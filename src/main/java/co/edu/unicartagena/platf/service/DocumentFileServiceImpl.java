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

import co.edu.unicartagena.platf.dao.controller.DocumentFileDao;
import co.edu.unicartagena.platf.dao.controller.DocumentFileDaoController;
import co.edu.unicartagena.platf.entity.DocumentFile;
import co.edu.unicartagena.platf.model.ErrorMessage;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
public class DocumentFileServiceImpl implements DocumentFileService {

    DocumentFileDao controller = new DocumentFileDaoController();
    
    private static DocumentFileService instance;

    private DocumentFileServiceImpl() {}

    public static DocumentFileService getInstance() {
        if (instance == null)
            instance = new DocumentFileServiceImpl();
        return instance;
    }

    @Override
    public DocumentFile add(DocumentFile document) {
        String msg = validateDocumentFile(document);
        if (msg != null) {
            ErrorMessage em = new ErrorMessage(400, msg);
            throw new BadRequestException(Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build());
        }
        return controller.save(document);
    }

    @Override
    public DocumentFile update(Integer id, DocumentFile entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DocumentFile remove(Integer id) {
        DocumentFile document = this.get(id);
        //remove file document
        controller.delete(id);
        return document;
    }

    @Override
    public DocumentFile get(Integer id) {
        DocumentFile document = controller.find(id);
        if (document == null) {
            String msg = String.format("The document with id %d not found.", id);
            ErrorMessage em = new ErrorMessage(404, msg);
            throw new NotFoundException(Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(em)
                    .build());
        }
        return document;
    }

    @Override
    public List<DocumentFile> getAll() {
        return this.controller.findAll();
    }

    private String validateDocumentFile(DocumentFile document) {
        String msg = null;
        if (document == null)
            msg = "The document object can't be null.";
        else if (document.getName() == null || "".equals(document.getName()))
            msg = "The file name is important, it can not be null.";
        return msg;
    }
    
}
