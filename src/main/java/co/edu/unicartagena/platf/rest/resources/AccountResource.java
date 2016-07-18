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

import co.edu.unicartagena.platf.model.Message;
import co.edu.unicartagena.platf.service.UserService;
import co.edu.unicartagena.platf.service.UserServiceImpl;
import co.edu.unicartagena.platf.transfer.TokenTransfer;
import co.edu.unicartagena.platf.transfer.LoginTransfer;
import co.edu.unicartagena.platf.transfer.ResetTransfer;
import java.util.logging.Level;

import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
@Path("accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    UserService service = UserServiceImpl.getInstance();

    private static final Logger LOG = Logger.getLogger(AccountResource.class.getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Authenticate please!";
    }
    
    @GET
    @Path("pin")
    public Message sendPin(@QueryParam("q") String email) {
        LOG.log(Level.INFO, "Email to send {0}", email);
        service.sendPin(email);
        return new Message(200, String.format("It sent a pin to %s", email));
    }

    @POST
    @Path("login")
    public Response authenticate(LoginTransfer loginTransfer) {
        if (loginTransfer == null)
            throw new BadRequestException("Information for login is required.");
        
        TokenTransfer token = new TokenTransfer(service.login(
                loginTransfer.getUsername(), loginTransfer.getPassword()));
        
        return Response.ok(token)
                .build();
    }

    @POST
    @Path("reset")
    public Message reset(ResetTransfer reset) {
        LOG.info(reset.toString());
        service.reset(reset.getEmail(), reset.getPassword(), reset.getPin());
        return new Message(200, "Change password successful!");
    }

}
