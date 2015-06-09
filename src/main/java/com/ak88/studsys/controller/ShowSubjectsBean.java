package com.ak88.studsys.controller;

import com.ak88.studsys.exceptions.SubjectAlreadyExistsException;
import com.ak88.studsys.model.Subject;
import com.ak88.studsys.service.interfaces.SubjectManagerLocal;
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
public class ShowSubjectsBean extends BaseController implements Serializable {
    static final Logger log = LoggerFactory.getLogger(ShowSubjectsBean.class);

    private String infoMessage;

    @EJB
    private SubjectManagerLocal subjectManager;

    List<Subject> subjects;
    List<Subject> selectedSubjects = new ArrayList<>();

    @Produces
    @Named
    @RequestScoped
    Subject newSubject = new Subject();


    Subject modSubject = new Subject();

    public Subject getModSubject() {
        return modSubject;
    }

    public void setModSubject(Subject modSubject) {
        this.modSubject = modSubject;
    }

    public Subject getNewSubject() {
        return newSubject;
    }

    public void setNewSubject(Subject newSubject) {
        this.newSubject = newSubject;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubject(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Subject> getSelectedSubjects() {
        return selectedSubjects;
    }

    public void setSelectedSubjects(List<Subject> selectedSubjects) {
        this.selectedSubjects = selectedSubjects;
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }


    public void showAllSubjects() {
        this.subjects = subjectManager.getSubjects();
        for (Subject s : subjects) {
            log.info("Log: subject: "+s);
        }
    }

    public String deleteSubjects() {
        for (Subject s : subjects) {
            log.info("Log: selected: " + s + " " + s.isIsSelected());
            if (s.isIsSelected()) {
                this.selectedSubjects.add(s);
            }
        }
        subjectManager.deleteSelectedSubjects(this.selectedSubjects);
        log.info("Log: Subjects deleted");
        return "subjects.xhtml?faces-redirect=true";
    }

    public String addNewSubject() {
        log.info("Log: NewSubject " + newSubject);
        try {
            subjectManager.addSubject(newSubject);
        } catch (SubjectAlreadyExistsException e) {
            setInfoMessage(e.MESSAGE);
        }
        log.info("Log: Subject " + newSubject + " added.");
        return "subjects.xhtml?faces-redirect=true";
    }

    public String modifySubject() {
        Subject subjectToMod = (Subject) getSession().getAttribute("subjectToMod");
        log.info("Log: subjectToMod got: "+subjectToMod);

            Subject subjectToModify = subjectManager.find(subjectToMod.getId());
            subjectToModify.setSubjectName(newSubject.getSubjectName());
            try {
                subjectManager.addSubject(subjectToModify);
            } catch (SubjectAlreadyExistsException e) {
                setInfoMessage(e.MESSAGE);
            }

            log.info("Log: Subject " + subjectToModify + " modified.");

        return "subjects.xhtml?faces-redirect=true";
    }


    public String check() {
        return (String) getSession().getAttribute("addOrMod");
    }


    public String toModSubject() {
        for (Subject s : subjects) {
            log.info("Log: selected: " + s + " " + s.isIsSelected());
            if (s.isIsSelected()) {
                this.selectedSubjects.add(s);
            }
        }
        if (this.selectedSubjects.isEmpty()) {
            return "subjects.xhtml?faces-redirect=true";
        }
        getSession().setAttribute("addOrMod", "mod");
        if (!selectedSubjects.isEmpty()) {
            this.modSubject = selectedSubjects.get(0);
            getSession().setAttribute("subjectToMod", this.modSubject);
        }

        return "addModifySubject.xhtml?faces-redirect=true";
    }

    public String toAddSubject() {
        getSession().setAttribute("addOrMod", "add");
        return "addModifySubject.xhtml?faces-redirect=true";
    }







    @PostConstruct
    public void setUp () {
        showAllSubjects();


    }
}
