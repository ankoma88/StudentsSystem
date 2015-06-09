package com.ak88.studsys.service;

import com.ak88.studsys.model.Progress;
import com.ak88.studsys.model.Student;
import com.ak88.studsys.model.Subject;
import com.ak88.studsys.model.Term;
import com.ak88.studsys.service.interfaces.ProgressManagerLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;


@Stateless
public class ProgressManager implements ProgressManagerLocal {
    static final Logger log = LoggerFactory.getLogger(ProgressManager.class);

    @PersistenceContext(unitName = "studentsDB")
    public EntityManager em;


    @Override
    public void instantiateProgresses() {
        em.createQuery("DELETE FROM Progress progress").executeUpdate();

        Query studQuery = em.createQuery("SELECT student FROM Student student", Student.class);
        List<Student> allStuds = studQuery.getResultList();

        Query subjQuery = em.createQuery("SELECT subject FROM Subject subject", Subject.class);
        List<Subject> allSubjs = subjQuery.getResultList();

        Random rand = new Random();

        for (Student stud : allStuds) {
            stud = em.getReference(Student.class, stud.getId());
            for (Subject subj : allSubjs) {
                Progress p = new Progress(rand.nextInt((100 - 1) + 1) + 1);
                subj = em.getReference(Subject.class, subj.getId());
                p.setStudent(stud);
                p.setSubject(subj);
                em.merge(p);
            }
        }
    }


    @Override
    public Progress find(int id) {
        return em.find(Progress.class, id);
    }

    @Override
    public Map<Subject, Integer> loadProgress(Student selStudent, Term modTerm) {
        Query query = em.createQuery("SELECT progress FROM Progress progress WHERE progress.student.id =:sId AND progress.subject.term.id =:tId");
        query.setParameter("sId", selStudent.getId());
        query.setParameter("tId", modTerm.getId());
        List<Progress> results = query.getResultList();
        if (results == null) {
            results = new ArrayList<>();
        }
        log.info("Log: found progress resultList: "+results);

        Map<Subject, Integer> map = new HashMap<>();
        for (Progress p : results) {
            map.put(p.getSubject(), p.getMark());
        }
        log.info("Log: progress map: "+map);
        return map;
    }
}





























