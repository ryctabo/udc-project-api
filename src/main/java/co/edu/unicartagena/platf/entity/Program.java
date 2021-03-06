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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.1-SNAPSHOT
 */
@Entity
@XmlRootElement
@XmlType(propOrder = {"id", "code", "name", "nomenclature", "facultyId"})
@NamedQueries({
    @NamedQuery(name = "program.findByFacultyId",
            query = "select p from Program p where p.facultyId = :facultyId")
})
public class Program implements IEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(length = 3, nullable = false, unique = true)
    private String code;
    
    @Column(length = 100, nullable = false)
    private String name;
    
    @Column(length = 3, nullable = false, unique = true)
    private String nomenclature;
    
    @Column(name = "faculty_id")
    @XmlElement(name = "faculty_id")
    private int facultyId;
    
    public Program() {
    }

    public Program(String code, String name, int facultyId) {
        this.code = code;
        this.name = name;
        this.facultyId = facultyId;
    }

    public Program(String code, String name, String nomenclature, int facultyId) {
        this.code = code;
        this.name = name;
        this.nomenclature = nomenclature;
        this.facultyId = facultyId;
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

    public String getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(String nomenclature) {
        this.nomenclature = nomenclature;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }
    
}
