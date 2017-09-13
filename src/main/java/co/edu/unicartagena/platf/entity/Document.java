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
package co.edu.unicartagena.platf.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Gustavo Pacheco <ryctabo@gmail.com>
 * @version 1.0-SNAPSHOT
 */
@Entity
@XmlRootElement
@XmlType(propOrder = {
    "id", "name", "type", "docFileId", "facultyId", "programId", "people",
    "exp", "created", "modified"
})
public class Document implements IEntity {

    @XmlEnum(String.class)
    public enum Type {
        
        @XmlEnumValue("DOCUMENTO") DOCUMENT("DOCUMENTO"),
        @XmlEnumValue("RESOLUCION") RESOLUTION("RESOLUCION"),
        @XmlEnumValue("ESTAMENTO") ESTATE("ESTAMENTO");

        private final String name;

        private Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doc_file_id", nullable = false)
    private DocumentFile docFile;
    
    @Transient
    @XmlElement(name = "doc_id")
    private int docFileId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;
    
    @Transient
    @XmlElement(name = "faculty_id")
    private int facultyId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "program_id")
    private Program program;
    
    @Transient
    @XmlElement(name = "program_id")
    private int programId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "document_person",
            joinColumns = @JoinColumn(name = "doc_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "person_id",
                    referencedColumnName = "id"))
    private List<Person> people;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Calendar exp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Calendar created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar modified;

    public Document() {
        this.people = new ArrayList<>();
        this.created = Calendar.getInstance();
        this.modified = Calendar.getInstance();
    }

    public Document(String name, Type type, DocumentFile docFile,
            Faculty faculty, Calendar exp) {
        this.name = name;
        this.type = type;
        this.docFile = docFile;
        this.docFileId = docFile.getId();
        this.faculty = faculty;
        this.facultyId = faculty.getId();
        this.exp = exp;
        this.people = new ArrayList<>();
        this.created = Calendar.getInstance();
        this.modified = Calendar.getInstance();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @XmlTransient
    public DocumentFile getDocFile() {
        return docFile;
    }

    public void setDocFile(DocumentFile docFile) {
        this.docFile = docFile;
        this.docFileId = docFile.getId();
    }
    
    public int getDocFileId() {
        return docFileId;
    }

    public void setDocFileId(int docFileId) {
        this.docFileId = docFileId;
    }

    @XmlTransient
    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
        this.facultyId = faculty.getId();
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }
    
    @XmlTransient
    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
        this.programId = program.getId();
    }
    
    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public Calendar getExp() {
        return exp;
    }

    public void setExp(Calendar exp) {
        this.exp = exp;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public Calendar getModified() {
        return modified;
    }

    public void setModified(Calendar modified) {
        this.modified = modified;
    }

}
