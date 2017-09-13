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
import co.edu.unicartagena.platf.entity.Program;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
public class ProgramDaoController extends EntityDao<Program, Integer>
        implements ProgramDao {

    public ProgramDaoController() {
        super(Program.class);
    }

    @Override
    public List<Program> findAllPaginated(int start, int size) {
        EntityManager entityManager = getEntityManager();
        try {
            CriteriaQuery criteriaQuery = entityManager
                    .getCriteriaBuilder()
                    .createQuery();
            
            criteriaQuery.select(criteriaQuery.from(Program.class));
            
            TypedQuery<Program> typedQuery = entityManager
                    .createQuery(criteriaQuery);
            
            return typedQuery.setFirstResult(start)
                    .setMaxResults(size)
                    .getResultList();
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    @Override
    public List<Program> findAllProgramsByFacultyId(int facultyId)
            throws NotCreatedEntityManagerException {
        List<Parameter> params = Arrays
                .asList(new Parameter("facultyId", facultyId));
        return executeNamedQueryForList("program.findByFacultyId", params);
    }
    
}
