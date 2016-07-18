/*
 * Copyright 2016 Gustavo Pacheco <ryctabo.wordpress.com>.
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

import co.edu.unicartagena.platf.dao.controller.FacultyDao;
import co.edu.unicartagena.platf.dao.controller.FacultyDaoController;
import co.edu.unicartagena.platf.dao.controller.ProgramDao;
import co.edu.unicartagena.platf.dao.controller.ProgramDaoController;
import co.edu.unicartagena.platf.dao.exception.NotCreatedEntityManagerException;
import co.edu.unicartagena.platf.entity.Faculty;
import co.edu.unicartagena.platf.entity.Program;
import co.edu.unicartagena.platf.model.Message;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
public class ProgramServiceImpl implements ProgramService {

    private static ProgramService instance;

    ProgramDao programController = new ProgramDaoController();

    FacultyDao facultyController = new FacultyDaoController();
    
    private static final Logger LOG = Logger.getLogger(ProgramServiceImpl.class.getName());

    private ProgramServiceImpl() {}

    public static ProgramService getInstance() {
        if (instance == null) {
            instance = new ProgramServiceImpl();
        }
        return instance;
    }

    @Override
    public Program add(Program program) {
        if (program == null) {
            throw new BadRequestException("The program is null, this is required.");
        } else if (program.getFacultyId() <= 0) {
            Message me = new Message(400, "The faculty id is null, "
                    + "this is required.");
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(me)
                    .build();
            throw new BadRequestException(response);
        } else {
            int facultyId = program.getFacultyId();
            Faculty faculty = facultyController.find(facultyId);
            if (faculty == null) {
                String msg = String
                        .format("The faculty with id %d not found.", facultyId);
                Message em = new Message(400, msg);
                Response response = Response.status(Response.Status.BAD_REQUEST)
                        .entity(em)
                        .build();
                throw new BadRequestException(response);
            }
        }
        return programController.save(program);
    }

    @Override
    public Program update(Integer id, Program program) {
        if (id == null || id <= 0) {
            Message em = new Message(400, "The program id is required.");
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
            throw new BadRequestException(response);
        } else {
            get(id);
        }
        if (program == null) {
            Message em = new Message(400, "The program entity is "
                    + "required, all attributes are necessary.");
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
            throw new BadRequestException(response);
        }
        program.setId(id);
        Program updateProgram = programController.save(program);
        return updateProgram;
    }

    @Override
    public Program remove(Integer id) {
        Program program = get(id);
        programController.delete(id);
        return program;
    }

    @Override
    public Program get(Integer id) {
        Program program = programController.find(id);
        if (program == null) {
            String msg = String.format("The program with id %d not found.", id);
            Message em = new Message(404, msg);
            Response response = Response.status(Response.Status.NOT_FOUND)
                    .entity(em)
                    .build();
            throw new NotFoundException(response);
        }
        return program;
    }

    @Override
    public List<Program> getAll() {
        return this.programController.findAll();
    }

    @Override
    public List<Program> getAllProgramPaginated(int start, int size) {
        if (start < 0) {
            String msg = String.format("The start parameter is equal to %d, "
                    + "it should not be less than 0.", start);
            Message em = new Message(400, msg);
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
            throw new BadRequestException(response);
        }
        if (size <= 0) {
            String msg = String.format("The size parameter is equal to %d, "
                    + "it must be greater than 0.", size);
            Message em = new Message(400, msg);
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
            throw new BadRequestException(response);
        }
        return this.programController.findAllPaginated(start, size);
    }

    @Override
    public List<Program> getAllProgramByFacultyId(Integer facultyId) {
        if (facultyId == null) {
            Message em = new Message(400, "The faculty id is null, "
                    + "it's required.");
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
            throw new BadRequestException(response);
        }
        
        try {
            return programController.findAllProgramsByFacultyId(facultyId);
        } catch (NotCreatedEntityManagerException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
}
