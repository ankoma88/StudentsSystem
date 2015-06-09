package com.ak88.studsys.service;

import com.ak88.studsys.exceptions.TermAlreadyExistsException;
import com.ak88.studsys.model.Subject;
import com.ak88.studsys.model.Term;
import com.ak88.studsys.service.interfaces.TermManagerLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


@Stateless
public class TermManager implements TermManagerLocal {
    static final Logger log = LoggerFactory.getLogger(TermManager.class);

    @PersistenceContext(unitName = "studentsDB")
    public EntityManager em;


    @Override
    public void addTerm(Term newTerm, List<Subject> selectedSubjects) throws TermAlreadyExistsException {
        Term persistedTerm = em.merge(newTerm);

        //deleting references to terms in old subjects
        List<Subject> oldSubjects = getSubjectsOfTerm(persistedTerm);
        for (Subject oldSub : oldSubjects) {
            oldSub.setTerm(null);
            em.merge(oldSub);
        }

        //setting term to each new selected subject
        for (Subject s : selectedSubjects) {
            s.setTerm(newTerm);
            persistedTerm.getSubjects().add(s);
            em.merge(persistedTerm);
            em.merge(s);
        }
    }

    @Override
    public List<Term> getTerms() {
        Query query = em.createQuery("SELECT term FROM Term term", Term.class);
        List<Term> result = query.getResultList();
        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }

    @Override
    public Term find(int id) {
        return em.find(Term.class, id);
    }


    @Override
    public void deleteSelectedTerm(Term selTerm) {
        log.info("Log: termId: "+selTerm.getId());
        Term term = em.getReference(Term.class, selTerm.getId());

        List<Subject> subjects = getSubjectsOfTerm(selTerm);
        for (Subject subject : subjects) {
            Query query = em.createQuery("DELETE FROM Progress progress WHERE progress.subject.id =:subId");
            query.setParameter("subId", subject.getId());
            query.executeUpdate();
        }

        em.remove(term);
        em.flush();
    }

    @Override
    public List<Subject> getSubjectsOfTerm(Term modTerm) {
        if (modTerm == null || modTerm.getId()==null) {
            return new ArrayList<Subject>();
        }
        Term mTerm = em.getReference(Term.class, modTerm.getId());
        if (mTerm == null) {
            return new ArrayList<Subject>();
        }
        Query query = em.createQuery("SELECT subject FROM Subject subject WHERE subject.term.id = :tId");
        query.setParameter("tId", mTerm.getId());
        List<Subject> result = query.getResultList();
        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }


}
