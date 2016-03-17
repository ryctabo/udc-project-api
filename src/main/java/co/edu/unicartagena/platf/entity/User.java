/*
 * Copyright 2016 rycta.
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
package co.edu.unicartagena.platf.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 0.2
 */
@Entity(name = "UserEntity")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements IEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Column(length = 12, unique = true)
    private String username;
    
    @Column(length = 50, unique = true, nullable = false)
    private String email;
    
    @Column(length = 18, nullable = false)
    private String password;
    
    @ElementCollection(targetClass = RoleType.class)
    @JoinTable(name = "role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_id")
    @Enumerated(EnumType.ORDINAL)
    private List<RoleType> roles;
    
    private boolean enabled;
    
    private boolean deleted;

    public User() {
        this.roles = new ArrayList<>();
        this.enabled = true;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.roles = new ArrayList<>();
        this.enabled = true;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = new ArrayList<>();
        this.enabled = true;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleType> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
