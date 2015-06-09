package com.ak88.studsys.model;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table
@Named
@RequestScoped
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private int studId;
    private String fName;
    private String lName;
    private String studGroup;
    private Date startDate;

    private Progress progress;

    private boolean isSelected;

    @Transient
    public boolean isIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Student() {
    }

    public Student(String fName, String lName) {

        this.fName = fName;
        this.lName = lName;
        this.studGroup = String.valueOf(1);
        this.startDate = new Date();
    }

    public Student(String fName, String lName, Integer studGroup, Date startDate) {
        this.fName = fName;
        this.lName = lName;
        this.studGroup = String.valueOf(studGroup);
        this.startDate = startDate;
    }

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getId() {
        return studId;
    }

    public void setId(Integer id) {
        this.studId = id;
    }

    @NotNull
    @Size(min = 1, max = 20)
    @Column
    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    @NotNull
    @Size(min = 1, max = 20)
    @Column
    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    @NotNull
    @Size(min = 1, max = 20)
    @Column
    public String getStudGroup() {
        return studGroup;
    }

    public void setStudGroup(String studGroup) {
        this.studGroup = studGroup;
    }

    @Temporal(TemporalType.DATE)
    @NotNull
    @Column
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String retrieveFormDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(this.startDate);
    }

    @OneToOne (fetch = FetchType.EAGER, mappedBy = "student", cascade = CascadeType.ALL)
    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        if (!fName.equals(student.fName)) return false;
        if (!lName.equals(student.lName)) return false;
        if (!startDate.equals(student.startDate)) return false;
        if (!studGroup.equals(student.studGroup)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fName.hashCode();
        result = 31 * result + lName.hashCode();
        result = 31 * result + studGroup.hashCode();
        result = 31 * result + startDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return fName + " " + lName;
    }
}
