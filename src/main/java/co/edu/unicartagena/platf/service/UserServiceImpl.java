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

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0
 */
public class UserServiceImpl implements UserService, LoginService {

    UserDao controller = new UserDaoController();
    
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
    public User add(User entity) {
        return null;
    }

    @Override
    public User update(Integer id, User entity) {
        return null;
    }

    @Override
    public User remove(Integer id) {
        return null;
    }

    @Override
    public User get(Integer id) {
        return id != null ? controller.find(id) : null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
    
    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        try {
            if (usernameOrEmail == null)
                return null;
            
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
            return user;
        } catch (NotCreatedEntityManagerException ex) {
//            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException("Error generating entity manager"
                    + " for connecting database.\n" + ex.getMessage(), ex);
        }
    }
    
    private static final String VALID_EMAIL_REGEX = "[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,3})";
    
    @Override
    public boolean login(String usernameOrEmail, String password) {
        try {
            return controller.login(usernameOrEmail, password);
        } catch (NotCreatedEntityManagerException ex) {
//            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException("Error generating entity manager "
                    + "for connecting database.\n" + ex.getMessage(), ex);
        }
    }
    
}
