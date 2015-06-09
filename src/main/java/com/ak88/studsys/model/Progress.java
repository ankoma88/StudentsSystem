package com.ak88.studsys.model;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table
@Named
@RequestScoped
public class Progress implements Serializable {
    private static final long serialVersionUID = 1L;

    private int progressId;
    private int mark;
    private Subject subject;
    private Student student;


    public Progress() {
    }

    public Progress(int mark) {
        this.mark = mark;
    }

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getId() {
        return progressId;
    }

    public void setId(Integer id) {
        this.progressId = id;
    }

    @Column
    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }


    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Progress)) return false;

        Progress progress = (Progress) o;

        if (mark != progress.mark) return false;
        if (progressId != progress.progressId) return false;
        if (student != null ? !student.equals(progress.student) : progress.student != null) return false;


        return true;
    }

    @Override
    public int hashCode() {
        int result = progressId;
        result = 31 * result + mark;
        result = 31 * result + (student != null ? student.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(progressId);
    }
}
