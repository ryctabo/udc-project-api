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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
public interface FileService {

    String PATH_BASE = System.getProperty("user.home");
    
    String FOLDER_BASE = "co.edu.unicartagena.platform";
    
    String FOLDER_IMAGE = "images";
    
    String FOLDER_DOCUMENTS = "documents";
    
    String FORMAT_FILE_IMAGE = "IMG_%08d.%s";
    
    String FORMAT_FILE_DOCUMENT = "DOC_%s.%s";
    
    String upload(InputStream inputStream,
            FormDataContentDisposition fileMetaData);    
    
    boolean delete(FileType type, String fileName);
    
    String getPathName(FileType type, String fileName);
    
    void write(InputStream in, final OutputStream out) throws IOException;
    
    enum FileType {
        DOCUMENT, IMAGE
    }
}
