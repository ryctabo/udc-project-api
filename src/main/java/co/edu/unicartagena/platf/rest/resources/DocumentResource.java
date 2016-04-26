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

import co.edu.unicartagena.platf.entity.Document;
import co.edu.unicartagena.platf.service.DocumentService;
import co.edu.unicartagena.platf.service.DocumentServiceImpl;

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
 * @version 1.0-SNAPSHOT
 */
@Path("documents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentResource {
    
    DocumentService service = DocumentServiceImpl.getInstance();
    
    @GET
    public List<Document> getAllDocuments() {
        return service.getAll();
    }
    
    @GET
    @Path("{documentId}")
    public Document getDocument(@PathParam("documentId") Integer id) {
        return service.get(id);
    }
    
    @POST
    public Response addNewDocument(Document document, @Context UriInfo uriInfo) {
        Document newDocument = service.add(document);
        String newId = String.valueOf(newDocument.getId());
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(DocumentResource.class)
                .path(newId)
                .build();
        return Response.created(uri)
                .entity(newDocument)
                .build();
    }
    
    @PUT
    @Path("{documentId}")
    public Document updateDocument(@PathParam("documentId") Integer id,
            Document document) {
        return service.update(id, document);
    }
    
    @DELETE
    @Path("{documentId}")
    public Document deleteDocument(@PathParam("documentId") Integer id) {
        return service.remove(id);
    }
    
}
