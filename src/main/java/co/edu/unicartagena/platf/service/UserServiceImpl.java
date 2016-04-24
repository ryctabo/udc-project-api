/*
 * Copyright 2016 Gustavo Pacheco.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.edu.unicartagena.platf.service;

import co.edu.unicartagena.platf.dao.controller.UserDao;
import co.edu.unicartagena.platf.dao.controller.UserDaoController;
import co.edu.unicartagena.platf.dao.exception.NotCreatedEntityManagerException;
import co.edu.unicartagena.platf.entity.RoleType;
import co.edu.unicartagena.platf.entity.User;
import co.edu.unicartagena.platf.model.ErrorMessage;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
public class UserServiceImpl implements UserService {

    UserDao controller = new UserDaoController();

    private static final String VALID_EMAIL_REGEX = "[_a-z0-9-]+(\\.[_a-z0-9-]+)"
            + "*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,3})";

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class.getName());

    private UserServiceImpl() {
        User admin = new User("admin", "admin@unicartagena.edu.co", "admin");
        admin.addRoles(RoleType.values());
        controller.save(admin);
    }

    private static UserServiceImpl instance;

    public static UserServiceImpl getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }

    @Override
    public User add(User user) {
        if (user == null) {
            ErrorMessage em = new ErrorMessage(400, "The user entity is required.");
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
            throw new BadRequestException(response);
        } else if (user.getRoles().isEmpty()) {
            ErrorMessage em = new ErrorMessage(400, "The user roles is required.");
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
            throw new BadRequestException(response);
        }
        return controller.save(user);
    }

    @Override
    public User update(Integer id, User user) {
        if (id == null || id <= 0) {
            ErrorMessage em = new ErrorMessage(400, "The user id is required.");
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
            throw new BadRequestException(response);
        } else {
            get(id);
        }
        
        if (user == null) {
            ErrorMessage em = new ErrorMessage(400, "The user information is "
                    + "required.");
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
            throw new BadRequestException(response);
        }
        user.setId(id);
        User updatedUser = controller.save(user);
        return updatedUser;
    }

    @Override
    public User remove(Integer id) {
        User findUser = get(id);
        controller.delete(id);
        return findUser;
    }

    @Override
    public User get(Integer id) {
        User findUser = controller.find(id);
        if (findUser == null) {
            String msg = String.format("The user with id %d not found.", id);
            ErrorMessage errorMessage = new ErrorMessage(404, msg);
            Response response = Response.status(Response.Status.NOT_FOUND)
                    .entity(errorMessage)
                    .build();
            throw new NotFoundException(response);
        }
        return findUser;
    }

    @Override
    public List<User> getAll() {
        return controller.findAll();
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        try {
            if (usernameOrEmail == null) {
                return null;
            }

            User user;

            Pattern pattern = Pattern.compile(VALID_EMAIL_REGEX);
            Matcher matcher = pattern.matcher(usernameOrEmail);

            if (matcher.find()) {
                LOG.info("Login ussing email");
                user = controller.findUserByEmail(usernameOrEmail);
            } else {
                LOG.info("Login ussing username");
                user = controller.findUserByUsername(usernameOrEmail);
            }
            
            if (user == null) {
                String msg = String.format("User with username %s is not found.", usernameOrEmail);
                ErrorMessage em = new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), msg);
                Response response = Response.status(Response.Status.NOT_FOUND)
                        .entity(em)
                        .build();
                throw new NotFoundException(response);
            }
            
            return user;
        } catch (NotCreatedEntityManagerException ex) {
            throw new WebApplicationException("Error generating entity manager"
                    + " for connecting database.\n" + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean login(String username, String password) {
        try {
            return controller.login(username, password);
        } catch (NotCreatedEntityManagerException ex) {
            throw new WebApplicationException("Error generating entity manager "
                    + "for connecting database.\n" + ex.getMessage(), ex);
        }
    }

}
