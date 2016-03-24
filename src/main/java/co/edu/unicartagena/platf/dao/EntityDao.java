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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @param <T> Entity Class
 * @param <I> Data type of primary key or id of entity
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 0.1
 */
public class EntityDao<T extends IEntity, I> implements DataAccessObject<T, I> {
    
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

    public EntityDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    /**
     * get entity manager factory
     * @return entity manager factory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return FACTORY;
    }
    
    /**
     * This method create a entity manager of a factory
     * @return entity manager
     */
    protected EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }
    
    @Override
    public List<T> findAll() {
        EntityManager entityManager = getEntityManager();
        try {
            CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(this.entityClass));
            
            TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
            return typedQuery.getResultList();
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    @Override
    public T find(I id) {
        if (id == null)
            return null;
        
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.find(this.entityClass, id);
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    @Override
    public T save(T entity) {
        EntityManager entityManager = getEntityManager();
        try {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            T entityCreated = entityManager.merge(entity);
            transaction.commit();
            return entityCreated;
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    @Override
    public void delete(I id) {
        if (id == null)
            return ;
        
        EntityManager entityManager = getEntityManager();
        try {
            if (entityManager != null) {
                EntityTransaction transaction = entityManager.getTransaction();
                transaction.begin();
                
                T entity = this.find(id);
                
                if (entity != null)
                    entityManager.remove(entity);
                
                transaction.commit();
            }
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }
    
}
