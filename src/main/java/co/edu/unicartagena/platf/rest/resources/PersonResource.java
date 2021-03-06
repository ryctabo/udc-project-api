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

package co.edu.unicartagena.platf.rest.resources;

import co.edu.unicartagena.platf.entity.Person;
import co.edu.unicartagena.platf.service.PersonService;
import co.edu.unicartagena.platf.service.PersonServiceImpl;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
@Path("persons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {
    
    PersonService service = PersonServiceImpl.getInstance();
    
    @GET
    public List<Person> getAllPeople(@QueryParam("like") String like) {
        if (like != null && !"".equals(like)) {
            service.findPersonByFullName(like);
        }
        return service.getAll();
    }
    
    @POST
    public Response addPerson(Person person, @Context UriInfo uriInfo) {
        Person newPerson = service.add(person);
        String newId = String.valueOf(newPerson.getId());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
        return Response.created(uri)
                .entity(newPerson)
                .build();
    }
    
    @PUT
    @Path("{personId}")
    public Person updatePerson(@PathParam("personId") Integer id, Person person) {
        return service.update(id, person);
    }
    
    @GET
    @Path("{personId}")
    public Person getPerson(@PathParam("personId") Integer personId) {
        return service.get(personId);
    }
    
}
