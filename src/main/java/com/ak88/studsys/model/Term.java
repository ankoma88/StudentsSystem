package com.ak88.studsys.model;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Named
@RequestScoped
public class Term implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer term_id;
    private Integer duration;
    private List<Subject> subjects;

    private boolean isSelected;


    public Term() {
    }

    public Term(Integer duration, List<Subject> subjects) {
        this.duration = duration;
        this.subjects = subjects;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getId() {
        return term_id;
    }

    public void setId(Integer id) {
        this.term_id = id;
    }


    @Column
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @OneToMany (fetch = FetchType.EAGER, mappedBy = "term", cascade = CascadeType.ALL)
    public List<Subject> getSubjects() {
        if (subjects == null) {
            subjects = new ArrayList<Subject>();
        }
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Transient
    public boolean isIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int result = duration.hashCode();
        result = 31 * result + subjects.hashCode();
        return result;
    }

    @Override
    public String toString() {
        if (term_id == null) {
            return "0";
        }
        return term_id.toString();
    }
}
