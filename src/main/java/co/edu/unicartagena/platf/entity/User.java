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
package co.edu.unicartagena.platf.entity;

import co.edu.unicartagena.platf.dao.converters.PasswordConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0
 */
@XmlRootElement
@Entity(name = "UserEntity")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries(value = {
    @NamedQuery(
            name = "user.findByUsername",
            query = "select u from UserEntity u where u.username = :username"),
    @NamedQuery(
            name = "user.findByEmail",
            query = "select u from UserEntity u where u.email = :email"),
    @NamedQuery(
            name = "user.login",
            query = "select u from UserEntity u where (u.username = :login or "
            + "u.email = :login) and u.password = :password and "
            + "u.enabled = true and u.deleted = false"),
    @NamedQuery(
            name = "user.findByRole",
            query = "select u from UserEntity u join u.roles r where r = :role")
})
@XmlType(propOrder = {
    "id", "username", "email", "password", "roles", "enabled", "deleted",
    "created"
})
public class User implements IEntity {

    @Id
    @XmlElement(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 12, unique = true)
    private String username;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Convert(converter = PasswordConverter.class)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_id", nullable = false)
    @JoinTable(name = "role", joinColumns = @JoinColumn(name = "user_id"))
    @ElementCollection(fetch = FetchType.EAGER, targetClass = RoleType.class)
    private List<RoleType> roles;

    private boolean enabled;

    private boolean deleted;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Calendar created;

    public User() {
        this.roles = new ArrayList<>();
        this.enabled = true;
        this.created = GregorianCalendar.getInstance();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.roles = new ArrayList<>();
        this.enabled = true;
        this.created = GregorianCalendar.getInstance();
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = new ArrayList<>();
        this.enabled = true;
        this.created = GregorianCalendar.getInstance();
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

    public void addRoles(RoleType... roles) {
        this.roles.addAll(Arrays.asList(roles));
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

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

}
