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
package co.edu.unicartagena.platf.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0
 */
public class MailLoader {

    private static final Logger LOG = Logger
            .getLogger(MailLoader.class.getName());

    private static final String PATH = "co/edu/unicartagena/platf/mail/%s.html";

    private String mail;

    public MailLoader(String filename) throws IOException {
        getFileString(filename);
    }

    private void getFileString(String filename) throws IOException {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            LOG.log(Level.INFO, "getFileString: get file with name: {0}",
                    filename);
            String path = String.format(PATH, filename);
            inputStream = getClass().getClassLoader().getResourceAsStream(path);
            reader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
            this.mail = reader.readLine();
        } finally {
            if (reader != null)
                reader.close();
            if (inputStream != null)
                inputStream.close();
        }
    }

    public MailLoader replace(String key, Object value) {
        LOG.log(Level.INFO, "replace: {0} -> {1}", new Object[]{key, value});
        String mKey = String.format("{{%s}}", key);
        this.mail = this.mail.replace(mKey, value.toString());
        return this;
    }

    public String getMail() {
        return mail;
    }

}
