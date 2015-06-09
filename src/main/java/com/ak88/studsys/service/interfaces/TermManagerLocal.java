package com.ak88.studsys.service.interfaces;

import com.ak88.studsys.exceptions.TermAlreadyExistsException;
import com.ak88.studsys.model.Subject;
import com.ak88.studsys.model.Term;

import javax.ejb.Local;
import java.util.List;

@Local
public interface TermManagerLocal {

    public List<Term> getTerms();
    public void addTerm(Term newTerm, List<Subject> selectedSubjects) throws TermAlreadyExistsException;
    public Term find(int id);
    public void deleteSelectedTerm(Term selTerms);

    List<Subject> getSubjectsOfTerm(Term modTerm);
}