package com.ak88.studsys.service.interfaces;

import com.ak88.studsys.model.Progress;
import com.ak88.studsys.model.Student;
import com.ak88.studsys.model.Subject;
import com.ak88.studsys.model.Term;

import javax.ejb.Local;
import java.util.Map;

@Local
public interface ProgressManagerLocal {

    void instantiateProgresses();

    Progress find(int id);

    Map<Subject, Integer> loadProgress(Student selStudent, Term modTerm);
}