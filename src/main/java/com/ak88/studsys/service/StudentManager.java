package com.ak88.studsys.service;

import com.ak88.studsys.exceptions.StudentAlreadyExistsException;
import com.ak88.studsys.exceptions.StudentNotFoundException;
import com.ak88.studsys.model.Student;
import com.ak88.studsys.service.interfaces.StudentManagerLocal;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


@Stateless
public class StudentManager implements StudentManagerLocal {

    @PersistenceContext(unitName = "studentsDB")
    public EntityManager em;


    @Override
    public Student getStudent(String lName, String fName) throws StudentNotFoundException {
        Query query = em.createQuery("SELECT student FROM Student student WHERE student.lName = :lastName AND student.fName = :firstName");
        query.setParameter("lastName", lName);
        query.setParameter("firstName", fName);
        Student student;
        try {
            student = (Student) query.getSingleResult();
        } catch (NoResultException e) {
            throw new StudentNotFoundException();
        }
        return student;
    }


    @Override
    public void addStudent(Student newStudent) throws StudentAlreadyExistsException {
        Query query = em.createQuery("SELECT student FROM Student student WHERE student.lName = :lastName AND student.fName = :firstName");
        query.setParameter("lastName", newStudent.getlName());
        query.setParameter("firstName", newStudent.getfName());
        try {
            query.getSingleResult();
            throw new StudentAlreadyExistsException();
        } catch (NoResultException e) {
            em.merge(newStudent);
            em.flush();
        }

    }


    @Override
    public List<Student> getStudents() {
        Query query = em.createQuery("SELECT student FROM Student student", Student.class);
        List<Student> result = query.getResultList();
        if (result == null) {
            return new ArrayList<>();
        }
        return result;

    }

    @Override
    public Student find(int id) {
        return em.find(Student.class, id);
    }

    @Override
    public void deleteSelectedStudents(List<Student> selStuds) {
        for (Student s : selStuds) {
            Student student = em.getReference(Student.class, s.getId());

            Query query = em.createQuery("DELETE FROM Progress progress WHERE progress.student.id =:studId");
            query.setParameter("studId", student.getId());
            query.executeUpdate();



            em.remove(student);
            em.flush();
        }
    }


}
