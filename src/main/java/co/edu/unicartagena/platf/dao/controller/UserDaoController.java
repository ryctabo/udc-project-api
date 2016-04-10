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
import co.edu.unicartagena.platf.dao.exception.UserNotFoundException;
import co.edu.unicartagena.platf.entity.User;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0
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
        String msg = String.format("The user with name %s was not found.", username);
        return executeNamedQuery(getEntityManager(), "user.findByUsername", parameters, msg);
    }

    @Override
    public User findUserByEmail(String email)
            throws NotCreatedEntityManagerException {
        List<Parameter> parameters = Arrays.asList(new Parameter("email", email));
        String errorMessage = String.format("The user with email %s was not found", email);
        return executeNamedQuery(getEntityManager(), "user.findByEmail", parameters, errorMessage);
    }

    @Override
    public User findUserByUsernameOrEmail(String argument)
            throws NotCreatedEntityManagerException {
        List<Parameter> params = Arrays.asList(new Parameter("signIn", argument));
        String errorMeesage = "The username or email is not found";
        return executeNamedQuery(getEntityManager(), "user.findByUsernameOrEmail", params, errorMeesage);
    }
    
    @Override
    public boolean login(String usernameOrEmail, String password)
            throws NotCreatedEntityManagerException {
        List<Parameter> params = Arrays.asList(
                new Parameter("login", usernameOrEmail),
                new Parameter("password", password)
        );
        String errorMsg = "The username or password is not valid";
        User user = executeNamedQuery(getEntityManager(), "user.login", params, errorMsg);
        return user != null;
    }

    /**
     * Get first user from named query with given params.
     * 
     * @param entityManager entity manager
     * @param namedQuery named query
     * @param parameters params for insert into the named query
     * @param errorMessage the detail message if ocurred a error
     * @return user
     * @throws UserNotFoundException if user not found
     * @throws NotCreatedEntityManagerException if entity user is null
     */
    private User executeNamedQuery(EntityManager entityManager, String namedQuery,
            List<Parameter> parameters, String errorMessage)
            throws NotCreatedEntityManagerException {
        if (entityManager != null) {
            try {
                TypedQuery<User> typedQuery = entityManager
                        .createNamedQuery(namedQuery, User.class);

                for (Parameter parameter : parameters) {
                    typedQuery.setParameter(parameter.key, parameter.value);
                }
                
                List<User> users = typedQuery.getResultList();
                if (users.isEmpty()) {
                    return null;
                }

                return users.iterator().next();
            } finally {
                entityManager.close();
            }
        } else {
            throw new NotCreatedEntityManagerException("The entity manager is "
                    + "null, entity manager factory does not create entity manager");
        }
    }

    /**
     * The class <code>Parameter</code> is a argument for insert in SQL, more 
     * exactly in named query.
     */
    private class Parameter {

        String key, value;

        /**
         * Create a param with key and value.
         * 
         * @param key id of value.
         * @param value object of type string.
         */
        public Parameter(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

}
