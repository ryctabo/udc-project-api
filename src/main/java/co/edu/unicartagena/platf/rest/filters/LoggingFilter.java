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

package co.edu.unicartagena.platf.rest.filters;

import co.edu.unicartagena.platf.model.Message;
import co.edu.unicartagena.platf.rest.TokenUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String AUTHORIZATION_PREFRIX = "Bearer ";

    private static final Logger LOG = Logger
            .getLogger(LoggingFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        Iterator<PathSegment> segments = request.getUriInfo()
                .getPathSegments()
                .iterator();

        PathSegment segment = segments.next();
        if ("accounts".equals(segment.getPath()))
            return ;

        String authHeader = request.getHeaderString(AUTHORIZATION_HEADER);
        if (authHeader != null && !authHeader.isEmpty()) {
            String authToken = authHeader.replace(AUTHORIZATION_PREFRIX, "");
            try {
                TokenUtil.UserInfo uInfo = TokenUtil.validateToken(authToken);
                
                if (uInfo.isAdmin())
                    return ;
                
                switch (segment.getPath()) {
                    case "programs":
                    case "faculties":
                        LOG.info(segment.getPath());
                        LOG.info(request.getMethod());
                        if ("GET".equals(request.getMethod()))
                            return ;
                        abort(request);
                        return ;
                    case "users":
                        switch (request.getMethod()) {
                            case "GET":
                            case "PUT":
                                if (!segments.hasNext()) {
                                    abort(request);
                                    return ;
                                }
                                String path = segments.next().getPath();
                                if (path.matches("[0-9]+") &&
                                        Integer.parseInt(path) == uInfo.getId())
                                    return ;
                                if (segments.hasNext() && uInfo.getUsername()
                                        .equals(segments.next().getPath()))
                                    return ;
                                abort(request);
                        }
                }
            } catch (JoseException | InvalidJwtException ex) {
                LOG.log(Level.SEVERE, null, ex);
                abort(request);
            }
        } else {
            abort(request);
        }
    }

    private void abort(ContainerRequestContext request) {
        request.abortWith(createResponse(Response.Status.UNAUTHORIZED,
                "User cannot access the resource."));
    }

    private Response createResponse(Response.Status status, String message) {
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new Message(status.getStatusCode(), message))
                .build();
    }

}
