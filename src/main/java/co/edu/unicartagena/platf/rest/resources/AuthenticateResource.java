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

import co.edu.unicartagena.platf.entity.Person;
import co.edu.unicartagena.platf.entity.RoleType;
import co.edu.unicartagena.platf.entity.User;
import co.edu.unicartagena.platf.exception.TokenNotGeneratedException;
import co.edu.unicartagena.platf.rest.TokenUtil;
import co.edu.unicartagena.platf.service.LoginService;
import co.edu.unicartagena.platf.service.UserService;
import co.edu.unicartagena.platf.service.UserServiceImpl;
import co.edu.unicartagena.platf.transfer.TokenTransfer;
import co.edu.unicartagena.platf.transfer.UserTransfer;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jose4j.lang.JoseException;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0
 */
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticateResource {
    
    LoginService service = UserServiceImpl.getInstance();
    
    private static final Logger LOG = Logger.getLogger(AuthenticateResource.class.getName());
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Authenticate please!";
    }
    
    @POST
    public Response authenticate(UserTransfer userTransfer) {
        String usernameOrEmail = userTransfer.getUsernameOrEmail();
        String password = userTransfer.getPassword();
        if (service.login(usernameOrEmail, password)) {
            User user = ((UserService) service).findByUsernameOrEmail(usernameOrEmail);
            String token;
            try {
                TokenUtil.UserInfo userInfo = new TokenUtil.UserInfo();
                userInfo.setId(user.getId());
                userInfo.setUsername(user.getUsername());
                userInfo.setEmail(user.getEmail());
                if (user instanceof Person) {
                    userInfo.setName(((Person) user).toString());
                } else if (user.getRoles().contains(RoleType.ADMINISTRATOR)) {
                    userInfo.setName("Admin user");
                } else {
                    userInfo.setName("Not define.");
                }
                token = TokenUtil.generateToken(userInfo);
            } catch (JoseException | TokenNotGeneratedException ex) {
                LOG.log(Level.SEVERE, null, ex);
                throw new WebApplicationException(ex.getMessage(), ex);
            }
            return Response
                    .ok(new TokenTransfer(token), MediaType.APPLICATION_JSON)
                    .build();
        } else {
            return Response
                    .ok("Username or password is not valid.", MediaType.TEXT_PLAIN)
                    .build();
        }
    }
    
}
