/*
 * Copyright 2016 Gustavo Pacheco.
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

import co.edu.unicartagena.platf.entity.UserDetail;
import co.edu.unicartagena.platf.entity.RoleType;
import co.edu.unicartagena.platf.entity.User;
import co.edu.unicartagena.platf.exception.TokenNotGeneratedException;
import co.edu.unicartagena.platf.model.ErrorMessage;
import co.edu.unicartagena.platf.rest.TokenUtil;
import co.edu.unicartagena.platf.service.UserService;
import co.edu.unicartagena.platf.service.UserServiceImpl;
import co.edu.unicartagena.platf.transfer.TokenTransfer;
import co.edu.unicartagena.platf.transfer.LoginTransfer;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.lang.JoseException;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
public class AuthenticateResource {

    UserService service = UserServiceImpl.getInstance();

    private static final Logger LOG = Logger.getLogger(AuthenticateResource.class.getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Authenticate please!";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(LoginTransfer loginTransfer) {
        if (loginTransfer == null) {
            ErrorMessage em = new ErrorMessage(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    "Information for login is required.");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(em)
                    .build();
        }

        String usernameOrEmail = loginTransfer.getUsername();
        String password = loginTransfer.getPassword();

        if (service.login(usernameOrEmail, password)) {
            User user = service.findByUsernameOrEmail(usernameOrEmail);
            String token;

            try {
                TokenUtil.UserInfo userInfo = new TokenUtil.UserInfo();
                userInfo.setId(user.getId());
                userInfo.setUsername(user.getUsername());
                userInfo.setEmail(user.getEmail());

                if (user instanceof UserDetail) {
                    userInfo.setName(((UserDetail) user).toString());
                } else if (user.getRoles().contains(RoleType.ADMINISTRATOR)) {
                    userInfo.setName("Admin user");
                } else {
                    userInfo.setName("Not define.");
                }

                token = TokenUtil.generateToken(userInfo);
            } catch (JoseException | TokenNotGeneratedException ex) {
                LOG.log(Level.SEVERE, null, ex);
                ErrorMessage em = new ErrorMessage(
                        Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                        ex.getMessage());
                Response response = Response
                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(em)
                        .build();
                throw new WebApplicationException(response);
            }
            return Response
                    .ok(new TokenTransfer(token))
                    .build();
        } else {
            ErrorMessage em = new ErrorMessage(
                    Response.Status.UNAUTHORIZED.getStatusCode(),
                    "Username or password is not valid.");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(em)
                    .build();
        }
    }

}
