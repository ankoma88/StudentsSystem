package com.ak88.studsys.service;

import com.ak88.studsys.exceptions.SubjectAlreadyExistsException;
import com.ak88.studsys.exceptions.SubjectNotFoundException;
import com.ak88.studsys.model.Subject;
import com.ak88.studsys.service.interfaces.SubjectManagerLocal;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


@Stateless
public class SubjectManager implements SubjectManagerLocal {

    @PersistenceContext(unitName = "studentsDB")
    public EntityManager em;


    @Override
    public Subject getSubject(String subjectName) throws SubjectNotFoundException {
        Query query = em.createQuery("SELECT subject FROM Subject subject WHERE subject.subjectName = :subjectName");
        query.setParameter("subjectName", subjectName);
        Subject subject;
        try {
            subject = (Subject) query.getSingleResult();
        } catch (NoResultException e) {
            throw new SubjectNotFoundException();
        }
        return subject;
    }


    @Override
    public void addSubject(Subject newSubject) throws SubjectAlreadyExistsException {
        Query query = em.createQuery("SELECT subject FROM Subject subject WHERE subject.subjectName = :subjectName");
        query.setParameter("subjectName", newSubject.getSubjectName());
        try {
            query.getSingleResult();
            throw new SubjectAlreadyExistsException();
        } catch (NoResultException e) {
            em.merge(newSubject);
            em.flush();
        }

    }


    @Override
    public List<Subject> getSubjects() {
        Query query = em.createQuery("SELECT subject FROM Subject subject", Subject.class);
        List<Subject> result = query.getResultList();
        if (result == null) {
            return new ArrayList<>();
        }
        return result;

    }

    @Override
    public Subject find(int id) {
        return em.find(Subject.class, id);
    }

    @Override
    public void deleteSelectedSubjects(List<Subject> selSubjects) {
        for (Subject s : selSubjects) {
            Subject subject = em.getReference(Subject.class, s.getId());

            Query query = em.createQuery("DELETE FROM Progress progress WHERE progress.subject.id =:subId");
            query.setParameter("subId", subject.getId());
            query.executeUpdate();

            em.remove(subject);
            em.flush();
        }
    }


}
