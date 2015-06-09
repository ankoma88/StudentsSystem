package com.ak88.studsys.service.interfaces;

import com.ak88.studsys.exceptions.StudentAlreadyExistsException;
import com.ak88.studsys.exceptions.StudentNotFoundException;
import com.ak88.studsys.model.Student;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StudentManagerLocal {

    public Student getStudent(String name, String password) throws StudentNotFoundException;
    public List<Student> getStudents();
    public void addStudent(Student newStudent) throws StudentAlreadyExistsException;
    public Student find(int id);

    public void deleteSelectedStudents(List<Student> selStuds);
}