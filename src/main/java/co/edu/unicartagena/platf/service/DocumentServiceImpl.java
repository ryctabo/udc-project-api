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

import co.edu.unicartagena.platf.dao.controller.DocumentDao;
import co.edu.unicartagena.platf.dao.controller.DocumentDaoController;
import co.edu.unicartagena.platf.entity.Document;
import co.edu.unicartagena.platf.entity.DocumentFile;
import co.edu.unicartagena.platf.entity.Faculty;
import co.edu.unicartagena.platf.entity.Person;
import co.edu.unicartagena.platf.entity.Program;
import co.edu.unicartagena.platf.model.ErrorMessage;
import java.util.Calendar;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
public class DocumentServiceImpl implements DocumentService {
    
    private static DocumentService instance;
    
    DocumentDao controller = new DocumentDaoController();
    
    DocumentFileService fileService = DocumentFileServiceImpl.getInstance();
    
    private DocumentServiceImpl(){}
    
    public static DocumentService getInstance() {
        if (instance == null)
            instance = new DocumentServiceImpl();
        return instance;
    }

    @Override
    public Document add(Document document) {
        validateIfBadRequest(document);
        
        DocumentFile documentFile = fileService.get(document.getDocFileId());
        document.setDocFile(documentFile);
        
        String nameDoc = generateNameDocument(document);
        document.setName(nameDoc);
        
        return controller.save(document);
    }

    @Override
    public Document update(Integer id, Document document) {
        if (id == null || id <= 0) {
            ErrorMessage em = new ErrorMessage(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    "The id of document is required.");
            throw new BadRequestException(Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build());
        } else {
            get(id);
        }
        validateIfBadRequest(document);
        document.setId(id);
        return controller.save(document);
    }

    @Override
    public Document remove(Integer id) {
        Document document = get(id);
        controller.delete(id);
        return document;
    }

    @Override
    public Document get(Integer id) {
        Document document = controller.find(id);
        if (document == null) {
            ErrorMessage em = new ErrorMessage(404,
                    String.format("Document with id %d is not found.", id));
            throw new NotFoundException(Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(em)
                    .build());
        }
        return document;
    }

    @Override
    public List<Document> getAll() {
        return controller.findAll();
    }
    
    @Override
    public String generateNameDocument(Document document) {
        String name = document.getFaculty().getNomenclature() + "_";
        if (document.getProgram() != null) {
            name += document.getProgram().getNomenclature() + "_";
        }
        name += String.format("%s_%08d", document.getType().getName(),
                document.getDocFile().getId());
        if (document.getPeople().size() == 1) {
            name += "_" + document.getPeople()
                    .iterator()
                    .next()
                    .toString()
                    .replace(" ", "_");
        }
        name = name.toUpperCase() + ".pdf";
        return name;
    }

    private void validateIfBadRequest(Document document) {
        String message = null;
        if (document == null) {
            message = "The document can not be null, is required.";
        } else if (document.getType() != null) {
            message = "The type document is required.";
        } else if (document.getDocFileId() <= 0) {
            message = "The document id is required.";
        } else if (document.getExp() != null) {
            message = "The faculty is required.";
        }

        if (message != null) {
            Response.Status status = Response.Status.BAD_REQUEST;
            ErrorMessage em = new ErrorMessage(status.getStatusCode(), message);
            throw new BadRequestException(Response
                    .status(status)
                    .entity(em)
                    .build());
        }
    }
    
    public static void main(String[] args) {
        DocumentService service = DocumentServiceImpl.getInstance();
        
        Faculty faculty = new Faculty("020", "INGENIERIA", "ING");
        Program program = new Program("022", "INGENIERIA DE SISTEMAS", "SIS", 0);
        
        Person person = new Person("0221010030", "GUSTAVO", "PACHECO GOMEZ");
        
        DocumentFile documentFile = new DocumentFile();
        documentFile.setId(6837043);
        Document document = new Document("", Document.Type.RESOLUTION,
                documentFile, faculty, Calendar.getInstance());
        document.setProgram(program);
        document.getPeople().add(person);
        
        String docName = service.generateNameDocument(document);
        System.out.println(docName);
    }

}
