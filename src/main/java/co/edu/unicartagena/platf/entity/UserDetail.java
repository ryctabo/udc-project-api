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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0
 */
@Entity
@XmlRootElement
@XmlType(propOrder = {"name", "lastName", "fullName"})
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class UserDetail extends User {
    
    @Column(length = 100)
    private String name;
    
    @Column(name = "last_name", length = 100)
    @XmlElement(name = "last_name")
    private String lastName;

    public UserDetail() {
    }

    public UserDetail(String email, String password) {
        super(email, password);
    }

    public UserDetail(String username, String email, String password) {
        super(username, email, password);
    }

    public UserDetail(String name, String lastName, String email, String password) {
        super(email, password);
        this.name = name;
        this.lastName = lastName;
    }

    public UserDetail(String name, String lastName, String username, String email, String password) {
        super(username, email, password);
        this.name = name;
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    @XmlElement(name = "full_name")
    public String getFullName() {
        return this.toString();
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.name, this.lastName);
    }
    
}
