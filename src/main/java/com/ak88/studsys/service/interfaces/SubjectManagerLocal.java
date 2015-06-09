package com.ak88.studsys.service.interfaces;

import com.ak88.studsys.exceptions.SubjectAlreadyExistsException;
import com.ak88.studsys.exceptions.SubjectNotFoundException;
import com.ak88.studsys.model.Subject;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SubjectManagerLocal {

    public Subject getSubject(String name) throws SubjectNotFoundException;
    public List<Subject> getSubjects();
    public void addSubject(Subject newSubject) throws SubjectAlreadyExistsException;
    public Subject find(int id);

    public void deleteSelectedSubjects(List<Subject> selSubjects);
}