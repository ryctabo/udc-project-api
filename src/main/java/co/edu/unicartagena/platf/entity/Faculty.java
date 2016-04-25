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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0
 */
@Entity
@XmlRootElement
@XmlType(propOrder = {"id", "code", "name"})
public class Faculty implements IEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(unique = true, length = 3, nullable = false)
    private String code;
    
    @Column(length = 50, nullable = false)
    private String name;
    
    @Column(length = 3, nullable = false, unique = true)
    private String nomenclature;
    
    @OneToMany
    @XmlTransient
    @JoinColumn(name = "faculty_id", referencedColumnName = "id")
    private final List<Program> programs;

    public Faculty() {
        this.programs = new ArrayList<>();
    }

    public Faculty(String code, String name) {
        this.code = code;
        this.name = name;
        this.programs = new ArrayList<>();
    }

    public Faculty(String code, String name, String nomenclature) {
        this.code = code;
        this.name = name;
        this.nomenclature = nomenclature;
        this.programs = new ArrayList<>();
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

    public List<Program> getPrograms() {
        return programs;
    }

    public void addProgram(Program program) {
        this.programs.add(program);
        if (program.getFacultyId() != this.id) {
            program.setFacultyId(this.id);
        }
    }
    
}
