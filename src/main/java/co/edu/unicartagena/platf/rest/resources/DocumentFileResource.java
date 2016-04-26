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

import co.edu.unicartagena.platf.entity.DocumentFile;
import co.edu.unicartagena.platf.model.ErrorMessage;
import co.edu.unicartagena.platf.service.DocumentFileService;
import co.edu.unicartagena.platf.service.DocumentFileServiceImpl;
import co.edu.unicartagena.platf.service.FileService;
import co.edu.unicartagena.platf.service.FileServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
@Path("files/documents")
public class DocumentFileResource {

    FileService fileService = new FileServiceImpl();

    DocumentFileService service = DocumentFileServiceImpl.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DocumentFile> getAllDocumentFiles() {
        return service.getAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadDocumentFile(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileMetaData,
            @Context UriInfo uriInfo) {
        String fileName = fileService.upload(fileInputStream, fileMetaData);

        if (fileName != null) {
            DocumentFile newDocumentFile = service.add(new DocumentFile(fileName));
            String newId = String.valueOf(newDocumentFile.getId());
            
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(DocumentFileResource.class)
                    .path(newId).build();
            
            return Response.created(uri)
                    .entity(newDocumentFile)
                    .build();
        } else {
            ErrorMessage em = new ErrorMessage(500, "the file name is null");
            throw new WebApplicationException(Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(em)
                    .build());
        }
    }
    
    @DELETE
    @Path("{documentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public DocumentFile removeDocumentFile(@PathParam("documentFile") Integer id) {
        return service.remove(id);
    }
    
    @GET
    @Path("{documentId}")
    public Response downloadDocumentFile(@PathParam("documentId") Integer id) {
        final DocumentFile documentFile = service.get(id);
        StreamingOutput streamingOutput = new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException,
                    WebApplicationException {
                String pathname = fileService.getPathName(
                        FileService.FileType.DOCUMENT,
                        documentFile.getName());
                FileInputStream in = new FileInputStream(new File(pathname));
                fileService.write(in, out);
            }
        };
        return Response.ok(streamingOutput)
                .type("application/pdf")
                .header("Content-Disposition", "filename=" + "document.pdf")
                .build();
    }

}
