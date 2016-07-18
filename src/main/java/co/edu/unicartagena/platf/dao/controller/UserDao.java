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

import co.edu.unicartagena.platf.dao.DataAccessObject;
import co.edu.unicartagena.platf.dao.exception.NotCreatedEntityManagerException;
import co.edu.unicartagena.platf.entity.RoleType;
import co.edu.unicartagena.platf.entity.User;
import java.util.List;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
public interface UserDao extends DataAccessObject<User, Integer> {
    
    /**
     * 
     * @param start
     * @param size
     * @return 
     */
    List<User> findAllPaginated(int start, int size);
    
    /**
     * 
     * @param role
     * @return 
     * @throws NotCreatedEntityManagerException 
     */
    List<User> findAllByRole(RoleType role)
            throws NotCreatedEntityManagerException;
    
    /**
     * 
     * @param role
     * @param start
     * @param size
     * @return
     * @throws NotCreatedEntityManagerException 
     */
    List<User> findAllByRole(RoleType role, int start, int size)
            throws NotCreatedEntityManagerException;
    
    /**
     * 
     * @param username
     * @return
     * @throws NotCreatedEntityManagerException 
     */
    User findUserByUsername(String username)
            throws NotCreatedEntityManagerException;
    
    /**
     * 
     * @param email
     * @return 
     * @throws NotCreatedEntityManagerException 
     */
    User findUserByEmail(String email)
            throws NotCreatedEntityManagerException;
    
    /**
     * 
     * @param usernameOrEmail
     * @param password
     * @return
     * @throws NotCreatedEntityManagerException 
     */
    boolean login(String usernameOrEmail, String password)
            throws NotCreatedEntityManagerException;
    
}
