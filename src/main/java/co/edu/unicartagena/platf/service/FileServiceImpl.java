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

import co.edu.unicartagena.platf.model.ErrorMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
public class FileServiceImpl implements FileService {

    public FileServiceImpl() {
    }
    
    public PathBuild getPathBuild() {
        return new PathBuild();
    }
    
    private static final Logger LOG = Logger.getLogger(FileServiceImpl.class.getName());

    @Override
    public String upload(InputStream fileInputStream,
            FormDataContentDisposition fileMetaData) {
        String fileName = fileMetaData.getFileName();
        
        if (fileName == null || !fileName.contains(".")) {
            ErrorMessage em = new ErrorMessage(500, "The file name is incorrect.");
            throw new WebApplicationException(Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(em)
                    .build());
        }
        
        String ext = fileName
                .substring(fileName.lastIndexOf('.') + 1)
                .toLowerCase();
        
        String newFileName = null;
        
        if (ext.matches("jpg|jpeg|png")) {
            throw new WebApplicationException("Not implement feature.");
        } else if (ext.matches("pdf")) {
            String serial = UUID.randomUUID().toString().replace("-", "");
            newFileName = String.format(FORMAT_FILE_DOCUMENT, serial, ext);
            
            PathBuild pathBuild = getPathBuild()
                    .path(FOLDER_BASE, FOLDER_DOCUMENTS);

            final String PATHNAME = pathBuild.build(newFileName);
            final String URL_FOLDER = pathBuild.build();

            saveFile(URL_FOLDER, PATHNAME, fileInputStream);
        } else {
            ErrorMessage em = new ErrorMessage(500,
                    "The format of file is invalid.");
            throw new WebApplicationException(Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(em)
                    .build());
        }
        
        return newFileName;
    }
    
    @Override
    public boolean delete(FileType type, String fileName) {
        String pathname = getPathName(type, fileName);
        File file = new File(pathname);
        if (file.exists())
            return file.delete();
        return true;
    }
    
    @Override
    public String getPathName(FileType type, String fileName) {
        return getPathBuild()
            .path(FOLDER_BASE)
            .path((type == FileType.DOCUMENT ? FOLDER_DOCUMENTS : FOLDER_IMAGE))
            .build(fileName);
    }
    
    private void saveFile(final String URL_FOLDER, final String PATHNAME,
            InputStream in) throws WebApplicationException {
        try {
            File folder = new File(URL_FOLDER);
            if (!folder.exists())
                folder.mkdirs();
            
            try (OutputStream out = new FileOutputStream(new File(PATHNAME))) {
                write(in, out);
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new WebApplicationException("Error while uploading file."
                    + " Please try again!!", ex);
        }
    }

    @Override
    public void write(InputStream in, final OutputStream out) throws IOException {
        int read;
        byte[] bytes = new byte[1024];
        while ((read = in.read(bytes)) != -1)
            out.write(bytes, 0, read);
        out.flush();
    }

    protected class PathBuild {
        
        private String path = "";
        
        public PathBuild path(String... paths) {
            for (String pathItem : paths)
                this.path += pathItem + System.getProperty("file.separator");
            return this;
        }
        
        public String build(String fileName) {
            return this.path + fileName;
        }
        
        public String build() {
            return this.path;
        }
        
    }
    
}
