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

/**
 *
 * @author Gustavo Pacheco
 * @version 1.0
 */
public class Person extends User {
    
    private String name;
    
    private String lastname;

    public Person() {
    }

    public Person(String email, String password) {
        super(email, password);
    }

    public Person(String username, String email, String password) {
        super(username, email, password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.name, this.lastname);
    }
    
}
