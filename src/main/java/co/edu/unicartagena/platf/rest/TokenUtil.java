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
package co.edu.unicartagena.platf.rest;

import co.edu.unicartagena.platf.entity.Person;
import co.edu.unicartagena.platf.entity.User;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
public class TokenUtil {
    
    private final RsaJsonWebKey jsonWebKey;
    
    private static TokenUtil instance;
    
    public static final String ARG_USER_ID = "user_id";
    public static final String ARG_EMAIL = "email";
    public static final String ARG_USERNAME = "username";
    public static final String ARG_NAME = "name";
    
    private TokenUtil() throws JoseException {
        jsonWebKey = RsaJwkGenerator.generateJwk(2048);
        jsonWebKey.setKeyId("mysecrect");
    }
    
    private static TokenUtil getInstance() throws JoseException {
        if (instance == null) {
            instance = new TokenUtil();
        }
        return instance;
    }

    private RsaJsonWebKey getJsonWebKey() {
        return jsonWebKey;
    }
    
    /**
     * Generate token from user.
     * 
     * @param user user
     * @return json web token
     * @throws JoseException problems :(
     */
    public static String generateToken(User user) throws JoseException {
        JwtClaims claims = createPayload(user);
        
        RsaJsonWebKey jsonKey = getInstance().getJsonWebKey();
        
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        
        jws.setKey(jsonKey.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        
        return jws.getCompactSerialization();
    }
    
    /**
     * 
     * @param token
     * @return
     * @throws JoseException
     * @throws InvalidJwtException 
     */
    public static UserInfo validateToken(String token) throws JoseException,
            InvalidJwtException {
        RsaJsonWebKey jsonKey = getInstance().getJsonWebKey();
        
        JwtConsumer consumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setVerificationKey(jsonKey.getKey())
                .build();
        
        UserInfo userInfo = 
                getUserInfoFromPayload(consumer.processToClaims(token));
        
        return userInfo;
    }

    /**
     * Get user information from payload.
     * 
     * @param claims payload
     * @return user information
     * @throws NumberFormatException if the user id is not number
     */
    private static UserInfo getUserInfoFromPayload(JwtClaims claims)
            throws NumberFormatException {
        String id = String.valueOf(claims.getClaimValue(ARG_USER_ID));
        String user = String.valueOf(claims.getClaimValue(ARG_USERNAME));
        String name = String.valueOf(claims.getClaimValue(ARG_NAME));
        String mail = String.valueOf(claims.getClaimValue(ARG_EMAIL));
        UserInfo userInfo = new UserInfo();
        userInfo.setId(Integer.parseInt(id));
        userInfo.setName(name);
        userInfo.setUsername(user);
        userInfo.setEmail(mail);
        return userInfo;
    }

    private static JwtClaims createPayload(User user) {
        JwtClaims claims = new JwtClaims();
        
        claims.setExpirationTimeMinutesInTheFuture(15);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBefore(NumericDate.now());
        
        claims.setClaim(ARG_USER_ID, String.valueOf(user.getId()));
        claims.setClaim(ARG_NAME, ((Person) user).toString());
        claims.setClaim(ARG_USERNAME, user.getUsername());
        claims.setClaim(ARG_EMAIL, user.getEmail());
        
        return claims;
    }
    
    /**
     * Information of user.
     */
    public static class UserInfo {
        
        private int id;
        
        private String username;
        
        private String email;
        
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
    }
    
}
