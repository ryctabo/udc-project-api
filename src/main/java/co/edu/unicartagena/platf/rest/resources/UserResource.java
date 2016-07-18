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

import co.edu.unicartagena.platf.entity.RoleType;
import co.edu.unicartagena.platf.entity.UserDetail;
import co.edu.unicartagena.platf.entity.User;
import co.edu.unicartagena.platf.rest.bean.UserFilterBean;
import co.edu.unicartagena.platf.service.UserService;
import co.edu.unicartagena.platf.service.UserServiceImpl;
import co.edu.unicartagena.platf.transfer.UserTransfer;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.BeanParam;
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
    
    private static final Logger LOG = Logger
            .getLogger(UserResource.class.getName());
    
    @GET
    public List<UserDetail> getAllUsers(@BeanParam UserFilterBean bean) {
        final List<UserDetail> users = new ArrayList<>();
        List<User> temp;
        if (bean.getRole() != null) {
            temp = bean.getSize() > 0 ? service.findByRole(RoleType.PROGRAM,
                    bean.getStart(),
                    bean.getSize()) : service.findByRole(RoleType.PROGRAM);
        } else {
            temp = bean.getSize() > 0 ? service.getAll(bean.getStart(),
                    bean.getSize()) : service.getAll();
        }
        
        for (User user : temp) {
            if (user instanceof UserDetail) {
                user.setPassword(null);
                users.add((UserDetail) user);
            }
        }
        return users;
    }
    
    @POST
    public Response addUser(UserDetail userPerson, @Context UriInfo uriInfo) {
        UserDetail newUserPerson = (UserDetail) service.add(userPerson);
        String username = newUserPerson.getUsername();
        URI uri = uriInfo.getAbsolutePathBuilder().path(username).build();
        return Response.created(uri)
                .entity(newUserPerson)
                .build();
    }
    
    @PUT
    @Path("{userId}")
    public UserDetail updateUser(@PathParam("userId") Integer userId,
            UserTransfer user) {
        return ((UserServiceImpl) service).update(userId, user);
    }
    
    @DELETE
    @Path("{userId}")
    public User deleteUser(@PathParam("userId") Integer userId) {
        return service.remove(userId);
    }
    
    @GET
    @Path("{userId}")
    public Response getUser(@PathParam("userId") Integer userId) {
        User user = service.get(userId);
        user.setPassword(null);
        if (user instanceof UserDetail) {
            UserDetail userDetail = (UserDetail) user;
            return Response.ok(userDetail).build();
        }
        return Response.ok(user).build();
    }
    
    @GET
    @Path("username/{username}")
    public Response getUserByUsername(@PathParam("username") String username) {
        User user = service.findByUsername(username);
        user.setPassword(null);
        if (user instanceof UserDetail) {
            UserDetail person = (UserDetail) user;
            return Response.ok(person).build();
        }
        return Response.ok(user).build();
    }
    
}
