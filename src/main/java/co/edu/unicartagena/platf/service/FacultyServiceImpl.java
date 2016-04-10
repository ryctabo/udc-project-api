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

import co.edu.unicartagena.platf.dao.controller.FacultyDao;
import co.edu.unicartagena.platf.dao.controller.FacultyDaoController;
import co.edu.unicartagena.platf.entity.Faculty;
import co.edu.unicartagena.platf.model.ErrorMessage;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

/**
 * 
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0
 */
public class FacultyServiceImpl implements FacultyService {
    
    FacultyDao controller = new FacultyDaoController();

    private static FacultyService instance;

    private FacultyServiceImpl() {}
    
    public static FacultyService getInstance() {
        if (instance == null) {
            instance = new FacultyServiceImpl();
        }
        return instance;
    }
    
    @Override
    public Faculty add(Faculty faculty) {
        if (faculty == null) {
            throw new BadRequestException();
        }
        return controller.save(faculty);
    }

    @Override
    public Faculty update(Integer id, Faculty faculty) {
        if (id == null || id <= 0) {
            ErrorMessage em = new ErrorMessage(400, "The id of faculty is required");
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
            throw new BadRequestException(response);
        }
        
        if (faculty == null) {
            ErrorMessage em = new ErrorMessage(400, "The faculty information is");
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
            throw new BadRequestException(response);
        }
        faculty.setId(id);
        controller.save(faculty);
        return faculty;
    }

    @Override
    public Faculty remove(Integer id) {
        Faculty faculty = this.get(id);
        if (faculty  == null) {
            launchFacultyNotFound(id);
        }
        controller.delete(id);
        return faculty;
    }

    @Override
    public Faculty get(Integer id) {
        Faculty faculty = controller.find(id);
        if (faculty == null) {
            launchFacultyNotFound(id);
        }
        return faculty;
    }

    private void launchFacultyNotFound(Integer id) throws NotFoundException {
        String msg = String.format("The faculty with id %d not found.", id);
        ErrorMessage em = new ErrorMessage(404, msg);
        Response response = Response.status(Response.Status.NOT_FOUND)
                .entity(em)
                .build();
        throw new NotFoundException(response);
    }

    @Override
    public List<Faculty> getAll() {
        return controller.findAll();
    }
    
}
