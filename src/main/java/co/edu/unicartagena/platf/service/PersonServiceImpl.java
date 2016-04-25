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

package co.edu.unicartagena.platf.service;

import co.edu.unicartagena.platf.dao.controller.PersonDao;
import co.edu.unicartagena.platf.dao.controller.PersonDaoController;
import co.edu.unicartagena.platf.entity.Person;
import co.edu.unicartagena.platf.model.ErrorMessage;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;


public class PersonServiceImpl implements PersonService {
    
    PersonDao controller = new PersonDaoController();
    
    private static PersonService instance;
    
    private PersonServiceImpl(){}

    public static PersonService getInstance() {
        if (instance == null)
            instance = new PersonServiceImpl();
        return instance;
    }

    @Override
    public Person add(Person person) {
        String message = validatePerson(person);
        if (message != null) {
            ErrorMessage em = new ErrorMessage(400, message);
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
            throw new BadRequestException(response);
        }
        
        return controller.save(person);
    }

    @Override
    public Person update(Integer id, Person person) {
        if (id == null || id == 0) {
            ErrorMessage em = new ErrorMessage(400, "The person id is required.");
            Response response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
            throw new BadRequestException(response);
        } else {
            get(id);
        }
        
        String errorMessage = validatePerson(person);
        if (errorMessage != null) {
            ErrorMessage em = new ErrorMessage(400, errorMessage);
            throw new BadRequestException(Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build());
        }
        person.setId(id);
        Person uPerson = controller.save(person);
        return uPerson;
    }

    @Override
    public Person remove(Integer id) {
        Person person = get(id);
        controller.delete(id);
        return person;
    }

    @Override
    public Person get(Integer id) {
        Person person = controller.find(id);
        if (person == null) {
            String msg = String.format("The person with id %d not found", id);
            ErrorMessage em = new ErrorMessage(404, msg);
            throw new NotFoundException(Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(em)
                    .build());
        }
        return person;
    }

    @Override
    public List<Person> getAll() {
        return this.controller.findAll();
    }

    private String validatePerson(Person person) {
        String message = null;
        if (person  == null)
            message = "Person object is required.";
        else if (person.getCode() == null)
            message = "The code property is required.";
        else if (person.getCode().matches("[0-9]+"))
            message = "The code property is a string with number only.";
        else if (!"".equals(person.getName()))
            message = "The name property is required.";
        else if (!"".equals(person.getLastName()))
            message = "The last name property is required.";
        return message;
    }
    
}
