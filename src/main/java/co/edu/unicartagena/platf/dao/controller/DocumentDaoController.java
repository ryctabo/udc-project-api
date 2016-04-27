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

package co.edu.unicartagena.platf.dao.controller;

import co.edu.unicartagena.platf.dao.EntityDao;
import co.edu.unicartagena.platf.entity.Document;

import java.util.List;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
public class DocumentDaoController extends EntityDao<Document, Integer>
        implements DocumentDao {

    public DocumentDaoController() {
        super(Document.class);
    }

    @Override
    public Document save(Document entity) {
        Document doc = super.save(entity);
        changeState(doc);
        return doc;
    }
    
    @Override
    public Document find(Integer id) {
        Document document = super.find(id);
        changeState(document);
        return document;
    }

    @Override
    public List<Document> findAll() {
        List<Document> documents = super.findAll();
        for (Document doc : documents)
            changeState(doc);
        return documents;
    }

    private void changeState(Document document) {
        if (document == null)
            return;
        document.setDocFileId(document.getDocFile().getId());
        document.setFacultyId(document.getFaculty().getId());
        if (document.getProgram() != null)
            document.setProgramId(document.getProgram().getId());
    }
    
}
