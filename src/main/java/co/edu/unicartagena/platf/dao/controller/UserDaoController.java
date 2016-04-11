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
package co.edu.unicartagena.platf.dao.controller;

import co.edu.unicartagena.platf.dao.EntityDao;
import co.edu.unicartagena.platf.dao.exception.NotCreatedEntityManagerException;
import co.edu.unicartagena.platf.entity.User;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 2.0
 */
public class UserDaoController extends EntityDao<User, Integer> implements UserDao {

    /**
     * Creates a new instances of <code>UserDaoController</code>.
     */
    public UserDaoController() {
        super(User.class);
    }

    @Override
    public User findUserByUsername(String username)
            throws NotCreatedEntityManagerException {
        List<Parameter> parameters = Arrays.asList(new Parameter("username", username));
        return executeNamedQuery("user.findByUsername", parameters);
    }

    @Override
    public User findUserByEmail(String email)
            throws NotCreatedEntityManagerException {
        List<Parameter> parameters = Arrays.asList(new Parameter("email", email));
        return executeNamedQuery("user.findByEmail", parameters);
    }

    @Override
    public User findUserByUsernameOrEmail(String argument)
            throws NotCreatedEntityManagerException {
        List<Parameter> params = Arrays.asList(new Parameter("signIn", argument));
        return executeNamedQuery("user.findByUsernameOrEmail", params);
    }
    
    @Override
    public boolean login(String usernameOrEmail, String password)
            throws NotCreatedEntityManagerException {
        List<Parameter> params = Arrays.asList(
                new Parameter("login", usernameOrEmail),
                new Parameter("password", password)
        );
        User user = executeNamedQuery("user.login", params);
        return user != null;
    }

}
