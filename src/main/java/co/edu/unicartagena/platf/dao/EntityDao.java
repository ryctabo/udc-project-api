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
package co.edu.unicartagena.platf.dao;

import co.edu.unicartagena.platf.dao.exception.NotCreatedEntityManagerException;
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
 * @version 1.2.1
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
            CriteriaQuery criteriaQuery = entityManager
                    .getCriteriaBuilder()
                    .createQuery();
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
                    entityManager.remove(entityManager.contains(entity) ?
                            entity : entityManager.merge(entity));

                transaction.commit();
            }
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    /**
     * Get first entity from named query with given params.
     *
     * @param namedQuery named query
     * @param parameters params for insert into the named query
     * @return entity class
     * @throws NotCreatedEntityManagerException if entity user is null
     */
    protected T executeNamedQuery(String namedQuery, List<Parameter> parameters)
            throws NotCreatedEntityManagerException {
        EntityManager entityManager = getEntityManager();
        if (entityManager != null) {
            try {
                TypedQuery<T> typedQuery = entityManager
                        .createNamedQuery(namedQuery, entityClass);

                for (Parameter parameter : parameters) {
                    typedQuery.setParameter(parameter.key, parameter.value);
                }

                return typedQuery.getSingleResult();
            } finally {
                entityManager.close();
            }
        } else {
            throw new NotCreatedEntityManagerException("The entity manager is "
                    + "null, entity manager factory does not create entity manager");
        }
    }

    /**
     * Get all entities from named query with given params.
     *
     * @param namedQuery named query
     * @param parameters params for insert into the named query
     * @return entity class
     * @throws NotCreatedEntityManagerException if entity user is null
     */
    protected List<T> executeNamedQueryForList(String namedQuery,
            List<Parameter> parameters)
            throws NotCreatedEntityManagerException {
        EntityManager entityManager = getEntityManager();
        if (entityManager != null) {
            try {
                TypedQuery<T> typedQuery = entityManager
                        .createNamedQuery(namedQuery, entityClass);

                for (Parameter parameter : parameters) {
                    typedQuery.setParameter(parameter.key, parameter.value);
                }

                return typedQuery.getResultList();
            } finally {
                entityManager.close();
            }
        } else {
            throw new NotCreatedEntityManagerException("The entity manager is "
                    + "null, entity manager factory does not create entity manager");
        }
    }
    
    /**
     * Get all entities from named query with given params.
     *
     * @param namedQuery named query
     * @param parameters params for insert into the named query
     * @param start first index of result list
     * @param size max result of list
     * @return entity class
     * @throws NotCreatedEntityManagerException if entity user is null
     */
    protected List<T> executeNamedQueryForList(String namedQuery,
            List<Parameter> parameters, int start, int size)
            throws NotCreatedEntityManagerException {
        EntityManager entityManager = getEntityManager();
        if (entityManager != null) {
            try {
                TypedQuery<T> typedQuery = entityManager
                        .createNamedQuery(namedQuery, entityClass);

                for (Parameter parameter : parameters) {
                    typedQuery.setParameter(parameter.key, parameter.value);
                }

                return typedQuery.setFirstResult(start)
                        .setMaxResults(size)
                        .getResultList();
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
    protected class Parameter {

        protected String key;

        protected Object value;

        /**
         * Create a param with key and value.
         *
         * @param key id of value.
         * @param value object of type string.
         */
        public Parameter(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

}
