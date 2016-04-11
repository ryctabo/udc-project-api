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

import co.edu.unicartagena.platf.entity.Program;
import co.edu.unicartagena.platf.rest.bean.ProgramFilterBean;
import co.edu.unicartagena.platf.service.ProgramService;
import co.edu.unicartagena.platf.service.ProgramServiceImpl;

import java.net.URI;
import java.util.List;
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
@Path("programs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProgramResource {

    ProgramService service = ProgramServiceImpl.getInstance();

    @GET
    public List<Program> getAll(@BeanParam ProgramFilterBean filterBean) {
        if (filterBean.getFacultyId() != null) {
            return service.getAllProgramByFacultyId(filterBean.getFacultyId());
        }
        if (filterBean.getSize() != null) {
            return service.getAllProgramPaginated(filterBean.getStart(),
                    filterBean.getSize());
        }
        return service.getAll();
    }

    @POST
    public Response addProgram(Program program, @Context UriInfo uriInfo) {
        Program newProgram = service.add(program);
        String newProgramId = String.valueOf(newProgram.getId());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newProgramId).build();
        return Response.created(uri)
                .entity(newProgram)
                .build();
    }
    
    @PUT
    @Path("{programId}")
    public Program updateProgram(@PathParam("programId") Integer id,
            Program program) {
        return service.update(id, program);
    }
    
    @DELETE
    @Path("{programId}")
    public Program deleteProgram(@PathParam("programId") Integer id) {
        return service.remove(id);
    }
    
    @GET
    @Path("{programId}")
    public Program get(@PathParam("programId") Integer id) {
        return service.get(id);
    }

}
