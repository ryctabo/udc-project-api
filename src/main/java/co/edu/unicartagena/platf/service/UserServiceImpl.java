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
import co.edu.unicartagena.platf.entity.UserDetail;
import co.edu.unicartagena.platf.model.ErrorMessage;
import co.edu.unicartagena.platf.transfer.UserTransfer;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final String VALID_EMAIL_REGEX = "[_a-z0-9-]"
            + "+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,3})";

    private static final Logger LOG = Logger
            .getLogger(UserServiceImpl.class.getName());

    private UserServiceImpl() {
        if (controller.findAll().isEmpty()) {
            User admin = new UserDetail("Administrator", null, "admin",
                    "admin@unicartagena.edu.co", "admin");
            admin.addRoles(RoleType.values());
            controller.save(admin);
        }
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
            throw new BadRequestException("The user entity is required.");
        } else if (user.getRoles().isEmpty()) {
            throw new BadRequestException("The user roles is required.");
        } else if (user.getEmail() == null || !isEmail(user.getEmail())) {
            throw new BadRequestException("The user email is required.");
        } else if (user.getPassword() == null) {
            throw new BadRequestException("The user password is required");
        }
        return controller.save(user);
    }

    /**
     * This method modify a user in database.
     *
     * @param id
     * @param user {@code User}
     * @return {@code User}
     * @deprecated Use {@link #update(java.lang.Integer,
     * co.edu.unicartagena.platf.transfer.UserTransfer) instead.
     */
    @Override
    @Deprecated
    public User update(Integer id, User user) {
        if (id == null || id <= 0) {
            throw new BadRequestException("The user id is required.");
        } else {
            get(id);
        }

        if (user == null) {
            throw new BadRequestException("The user information is required.");
        }

        user.setId(id);
        return controller.save(user);
    }

    /**
     * This method update a user in database.
     *
     * @param id identifier of user
     * @param userTransfer user change within the database
     * @return user updated
     *
     * @see UserTransfer
     * @see UserDao#save(co.edu.unicartagena.platf.entity.User)
     * @see #get(java.lang.Integer)
     */
    public UserDetail update(Integer id, UserTransfer userTransfer) {
        UserDetail user = null;

        if (userTransfer == null) {
            throw new BadRequestException("The user information is required.");
        }
        if (id == null || id <= 0) {
            throw new BadRequestException("The user id is required.");
        } else {
            user = (UserDetail) get(id);
        }

        if (userTransfer.getEmail() != null) {
            if (isEmail(userTransfer.getEmail())) {
                user.setEmail(userTransfer.getEmail());
            } else {
                throw new BadRequestException("Email is incorrect");
            }
        }

        if (userTransfer.getLastName() != null) {
            user.setLastName(userTransfer.getLastName());
        }

        if (userTransfer.getName() != null) {
            user.setName(userTransfer.getName());
        }

        if (userTransfer.getUsername() != null) {
            user.setEmail(userTransfer.getUsername());
        }

        return (UserDetail) controller.save(user);
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
    public List<User> getAll(int start, int size) {
        return controller.findAllPaginated(start, size);
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

    @Override
    public User findByUsername(String username) {
        try {
            if (username == null) {
                throw new BadRequestException("The username is required");
            }

            User user = controller.findUserByUsername(username);

            if (user == null) {
                String msg = String.format(
                        "User with username %s is not found.",
                        username);
                ErrorMessage em = new ErrorMessage(
                        Response.Status.NOT_FOUND.getStatusCode(),
                        msg);
                Response response = Response.status(em.getCode())
                        .entity(em)
                        .build();
                throw new WebApplicationException(response);
            }

            return user;
        } catch (NotCreatedEntityManagerException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new WebApplicationException("Error generating entity manager "
                    + "for connecting database.\n" + ex.getMessage(), ex);
        }
    }

    private boolean isEmail(String email) {
        return Pattern.compile(VALID_EMAIL_REGEX).matcher(email).find();
    }

    @Override
    public User findByEmail(String email) {
        try {
            if (email == null) {
                throw new BadRequestException("The email is required");
            }

            if (!isEmail(email)) {
                throw new BadRequestException("Email is incorrect");
            }

            User user = controller.findUserByEmail(email);

            if (user == null) {
                String msg = String.format(
                        "User with email %s is not found.",
                        email);
                ErrorMessage em = new ErrorMessage(
                        Response.Status.NOT_FOUND.getStatusCode(),
                        msg);
                Response response = Response.status(em.getCode())
                        .entity(em)
                        .build();
                throw new WebApplicationException(response);
            }

            return user;
        } catch (NotCreatedEntityManagerException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new WebApplicationException("Error generating entity manager "
                    + "for connecting database.\n" + ex.getMessage(), ex);
        }
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        return isEmail(usernameOrEmail) ? findByEmail(usernameOrEmail)
                : findByUsername(usernameOrEmail);
    }

    @Override
    public List<User> findByRole(RoleType role) {
        try {
            return controller.findAllByRole(role);
        } catch (NotCreatedEntityManagerException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new WebApplicationException("Error generating entity manager "
                    + "for connecting database.\n" + ex.getMessage(), ex);
        }
    }

    @Override
    public List<User> findByRole(RoleType role, int start, int size) {
        try {
            return controller.findAllByRole(role, start, size);
        } catch (NotCreatedEntityManagerException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new WebApplicationException("Error generating entity manager "
                    + "for connecting database.\n" + ex.getMessage(), ex);
        }
    }

}
