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

package co.edu.unicartagena.platf.rest.mapper;

import co.edu.unicartagena.platf.model.ErrorMessage;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Context
    UriInfo uriInfo;
    
    @Override
    public Response toResponse(NotFoundException e) {
        String path = "not url";
        if (uriInfo != null) {
            path = uriInfo.getPath();
        }
        String msg = String.format("HTTP 404 Not found, the resource (%s) you "
                + "are trying to access is not found.", path);
        ErrorMessage errorMessage = new ErrorMessage(404, msg);
        return Response.fromResponse(e.getResponse())
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
