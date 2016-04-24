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
package co.edu.unicartagena.platf.rest.resources;

import co.edu.unicartagena.platf.entity.Person;
import co.edu.unicartagena.platf.entity.User;
import co.edu.unicartagena.platf.service.UserService;
import co.edu.unicartagena.platf.service.UserServiceImpl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    
    UserService service = UserServiceImpl.getInstance();
    
    @GET
    public List<Person> getAllUsers() {
        final List<Person> people = new ArrayList<>();
        for (User user : service.getAll()) {
            if (user instanceof Person) {
                people.add((Person) user);
            }
        }
        return people;
    }
    
    @POST
    public Response addUser(Person userPerson, @Context UriInfo uriInfo) {
        Person newUserPerson = (Person) service.add(userPerson);
        String username = newUserPerson.getUsername();
        URI uri = uriInfo.getAbsolutePathBuilder().path(username).build();
        return Response.created(uri)
                .entity(newUserPerson)
                .build();
    }
    
    @PUT
    @Path("{userId}")
    public Person updateUser(@PathParam("userId") Integer userId, Person person) {
        return (Person) service.update(userId, person);
    }
    
    @DELETE
    @Path("{userId}")
    public User deleteUser(@PathParam("userId") Integer userId) {
        return service.remove(userId);
    }
    
    @GET
    @Path("{username}")
    public Response getUserByUsername(@PathParam("username") String username) {
        User user = service.findByUsernameOrEmail(username);
        if (user instanceof Person) {
            Person person = (Person) user;
            return Response.ok(person).build();
        }
        return Response.ok(user).build();
    }
    
    @Path("login")
    public AuthenticateResource getAuthenticate() {
        return new AuthenticateResource();
    }
    
}
