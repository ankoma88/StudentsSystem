package com.ak88.studsys.controller;

import com.ak88.studsys.exceptions.StudentAlreadyExistsException;
import com.ak88.studsys.model.Student;
import com.ak88.studsys.service.interfaces.StudentManagerLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class ShowStudentsBean extends BaseController implements Serializable {
    static final Logger log = LoggerFactory.getLogger(ShowStudentsBean.class);

    private String infoMessage;

    @EJB
    private StudentManagerLocal studentManager;

    List<Student> students;
    List<Student> selectedStudents = new ArrayList<>();

    @Produces
    @Named
    @RequestScoped
    Student newStudent = new Student();


    Student modStudent = new Student();

    public Student getModStudent() {
        return modStudent;
    }

    public void setModStudent(Student modStudent) {
        this.modStudent = modStudent;
    }

    public Student getNewStudent() {
        return newStudent;
    }

    public void setNewStudent(Student newStudent) {
        this.newStudent = newStudent;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Student> getSelectedStudents() {
        return selectedStudents;
    }

    public void setSelectedStudents(List<Student> selectedStudents) {
        this.selectedStudents = selectedStudents;
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }


    public void showAllStudents() {
        this.students = studentManager.getStudents();
        for (Student s : students) {
            log.info("Log: student: "+s);
        }
    }

    public void chooseStudents() {
        for (Student s : students) {
            log.info("Log: selected: " + s + " " + s.isIsSelected());
            if (s.isIsSelected()) {
                this.selectedStudents.add(s);
            }
        }
    }

    public String deleteStudents() {
        chooseStudents();
        studentManager.deleteSelectedStudents(this.selectedStudents);
        log.info("Log: Students deleted");
        return "students.xhtml?faces-redirect=true";
    }

    public String addNewStudent() {
        log.info("Log: NewStudent " + newStudent);
        try {
            studentManager.addStudent(newStudent);
        } catch (StudentAlreadyExistsException e) {
            setInfoMessage(e.MESSAGE);
        }
        log.info("Log: Student " + newStudent + " added.");
        return "students.xhtml?faces-redirect=true";
    }

    public String modifyStudent() {
        Student studentToMod = (Student) getSession().getAttribute("studentToMod");
        log.info("Log: studToMod got: "+studentToMod);

            Student studentToModify = studentManager.find(studentToMod.getId());
            studentToModify.setfName(newStudent.getfName());
            studentToModify.setlName(newStudent.getlName());
            studentToModify.setStudGroup(newStudent.getStudGroup());
            studentToModify.setStartDate(newStudent.getStartDate());
            try {
                studentManager.addStudent(studentToModify);
            } catch (StudentAlreadyExistsException e) {
                setInfoMessage(e.MESSAGE);
            }

            log.info("Log: Student " + studentToModify + " modified.");

        return "students.xhtml?faces-redirect=true";
    }


    public String check() {
        return (String) getSession().getAttribute("addOrMod");
    }


    public String toModStudent() {
        chooseStudents();
        if (this.selectedStudents.isEmpty()) {
            return "students.xhtml?faces-redirect=true";
        }
        getSession().setAttribute("addOrMod", "mod");
        if (!selectedStudents.isEmpty()) {
            this.modStudent = selectedStudents.get(0);
            getSession().setAttribute("studentToMod", this.modStudent);
        }

        return "addModifyStudent.xhtml?faces-redirect=true";
    }

    public String toAddStudent() {
        getSession().setAttribute("addOrMod", "add");
        return "addModifyStudent.xhtml?faces-redirect=true";
    }

    public String toProgress() {
        chooseStudents();
        log.info("Log: selectedStudents: "+selectedStudents);
        if (this.selectedStudents.isEmpty()) {
            return "students.xhtml?faces-redirect=true";
        }
        getSession().setAttribute("addOrMod", "mod");
        if (!selectedStudents.isEmpty()) {
            this.modStudent = selectedStudents.get(0);
            getSession().setAttribute("studentToMod", this.modStudent);
        }

        return "progress.xhtml?faces-redirect=true";
    }






    @PostConstruct
    public void setUp () {
        showAllStudents();


    }
}
