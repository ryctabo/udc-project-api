/*
 * Copyright 2016 Gustavo Pacheco <ryctabo@gmail.com>.
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
package co.edu.unicartagena.platf.dao;

import co.edu.unicartagena.platf.entity.IEntity;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @param <T> Entity Class
 * @param <I> Data type of primary key or id of entity
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 0.1
 */
public class EntityDAO<T extends IEntity, I> implements DataAccessObject<T, I> {
    
    /**
     * Entity class.
     */
    protected Class<T> entityClass;
    
    /**
     * the name of persistence unit of datasource for connection in the database.
     */
    public static final String PERSISTENCE_UNIT = "unicartagenaPU";

    /**
     * The entity manager factory created by persistence unit.
     */
    private static final EntityManagerFactory FACTORY = 
            Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

    public EntityDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    @Override
    public List<T> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T find(I id) {
        throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T save(T entity) {
        throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(I id) {
        throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }
    
}
