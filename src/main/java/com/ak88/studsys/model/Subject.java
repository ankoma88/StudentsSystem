package com.ak88.studsys.model;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table
@Named
@RequestScoped
public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;

    private int subjectId;
    private String subjectName;
    private boolean isSelected;
    private Term term;

    private Progress progress;

    public Subject() {
    }

    public Subject(String subjectName) {
        this.subjectName = subjectName;
    }


    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getId() {
        return subjectId;
    }

    public void setId(Integer id) {
        this.subjectId = id;
    }

    @NotNull
    @Size(min = 1, max = 20)
    @Column
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subject_name) {
        this.subjectName = subject_name;
    }

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn
    public Term getTerm(){
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    @OneToOne (fetch = FetchType.EAGER, mappedBy = "subject", cascade = CascadeType.ALL)
    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    @Transient
    public boolean isIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;

        Subject subject = (Subject) o;

        if (!subjectName.equals(subject.subjectName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return subjectName.hashCode();
    }

    @Override
    public String toString() {
        return subjectName;
    }
}
