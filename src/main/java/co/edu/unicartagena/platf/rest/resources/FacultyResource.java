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

import co.edu.unicartagena.platf.entity.Faculty;
import co.edu.unicartagena.platf.service.FacultyService;
import co.edu.unicartagena.platf.service.FacultyServiceImpl;

import java.net.URI;
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
 * @version 1.0
 */
@Path("faculties")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FacultyResource {
    
    FacultyService service = FacultyServiceImpl.getInstance();
    
    @GET
    public List<Faculty> getAllFaculties() {
        return service.getAll();
    }
    
    @POST
    public Response addFaculty(Faculty faculty, @Context UriInfo uriInfo) {
        Faculty newFaculty = service.add(faculty);
        String newId = String.valueOf(newFaculty.getId());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
        return Response.created(uri)
                .entity(newFaculty)
                .build();
    }
    
    @PUT
    @Path("{facultyId}")
    public Faculty updateFaculty(@PathParam("facultyId") Integer id, Faculty faculty) {
        return service.update(id, faculty);
    }
    
    @DELETE
    @Path("{facultyId}")
    public Faculty removeFaculty(@PathParam("facultyId") Integer id) {
        return service.remove(id);
    }
    
    @GET
    @Path("{facultyId}")
    public Faculty getFaculty(@PathParam("facultyId") Integer id) {
        return service.get(id);
    }
    
}
