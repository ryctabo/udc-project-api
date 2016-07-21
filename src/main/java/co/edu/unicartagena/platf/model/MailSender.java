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

import java.io.UnsupportedEncodingException;

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0
 */
public class MailSender implements Runnable {

    private static final Logger LOG = Logger
            .getLogger(MailSender.class.getName());

    private static final String MAIL = "udcplatform@gmail.com";

    private static final String PASSWORD = "UdcPlatf2016";

    private static final String PROPERTY_HOST = "mail.smtp.host";

    private static final String PROPERTY_SOCKET_PORT = "mail.smtp.socketFactory.port";

    private static final String PROPERTY_SOCKET_CLASS = "mail.smtp.socketFactory.class";

    private static final String PROPERTY_AUTHORIZATION = "mail.smtp.auth";

    private static final String PROPERTY_PORT = "mail.smtp.port";

    private static final String PROPERTY_USER = "mail.smtp.user";

    private static final String PROPERTY_PASSWORD = "mail.smtp.password";

    private javax.mail.Message message;

    private Transport transport;

    private Properties props;

    private MailSender() {
        this.props = new Properties();
        this.props.put(PROPERTY_HOST, "smtp.gmail.com");
        this.props.put(PROPERTY_USER, MAIL);
        this.props.put(PROPERTY_PASSWORD, "smtp.gmail.com");
        this.props.put(PROPERTY_SOCKET_PORT, "465");
        this.props.put(PROPERTY_SOCKET_CLASS, "javax.net.ssl.SSLSocketFactory");
        this.props.put(PROPERTY_AUTHORIZATION, "true");
        this.props.put(PROPERTY_PORT, "465");
    }

    private static MailSender instance = new MailSender();

    public static MailSender instance()
            throws MessagingException {
        instance.create();
        return instance;
    }

    private MailSender create()
            throws MessagingException {
        Session session = Session.getDefaultInstance(props, null);
        this.message = new MimeMessage(session);
        try {
            this.message.setFrom(new InternetAddress(MAIL, "UDC Platform",
                    "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, "Unsupported encoding UTF-8", ex);
            throw new MessagingException("Unsupported encoding UTF-8, "
                    + ex.getMessage());
        }
        this.transport = session.getTransport();
        return instance;
    }

    public MailSender to(String email) throws MessagingException {
        this.message.setRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(email));
        return instance;
    }

    public MailSender subject(String subject) throws MessagingException {
        this.message.setSubject(subject);
        return instance;
    }

    public MailSender body(String message, String type) throws MessagingException {
        this.message.setContent(message, type);
        return instance;
    }

    public void send() throws MessagingException {
        String allRecipients = Arrays.toString(this.message.getAllRecipients());
        new Thread(this, "Sent to: " + allRecipients).start();
        LOG.log(Level.INFO, "Send mail to: {0}", allRecipients);
    }

    @Override
    public void run() {
        try {
            transport.connect("smtp.gmail.com", MAIL, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (transport != null)
                    transport.close();
            } catch (MessagingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

}
