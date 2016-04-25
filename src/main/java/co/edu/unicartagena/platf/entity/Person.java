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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "person.findByLike", 
            query = "select p from Person p where p.name like :search or "
                    + "p.lastName like :search")
})
public class Person implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, length = 10, nullable = true)
    private String code;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    public Person() {
    }

    public Person(String code, String name, String lastName) {
        this.code = code;
        this.name = name;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    @Override
    public String toString() {
        return String.format("%s %s", name, lastName);
    }
    
}
