package com.ak88.studsys.controller;

import com.ak88.studsys.exceptions.TermAlreadyExistsException;
import com.ak88.studsys.model.Subject;
import com.ak88.studsys.model.Term;
import com.ak88.studsys.service.interfaces.SubjectManagerLocal;
import com.ak88.studsys.service.interfaces.TermManagerLocal;
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
public class ShowTermsBean extends BaseController implements Serializable {
    static final Logger log = LoggerFactory.getLogger(ShowTermsBean.class);

    private String infoMessage;

    @EJB
    private TermManagerLocal termManager;

    @EJB
    private SubjectManagerLocal subjectManager;

    private List<Term> terms = new ArrayList<>();
    private List<Term> selectedTerms = new ArrayList<>();

    private List<Subject> termSubjects = new ArrayList<>();

    private List<Subject> allSubjects = new ArrayList<>();

    private List<Subject> selectedSubjects = new ArrayList<>();

    @Produces
    @Named
    @RequestScoped
    Term newTerm = new Term();

    String modTermId;
    Term modTerm = new Term();


    public String getModTermId() {
        return modTermId;
    }

    public void setModTermId(String modTermId) {
        this.modTermId = modTermId;
    }

    public List<Subject> getSelectedSubjects() {
        return selectedSubjects;
    }

    public void setSelectedSubjects(List<Subject> selectedSubjects) {
        this.selectedSubjects = selectedSubjects;
    }

    public List<Subject> getAllSubjects() {
        return allSubjects;
    }

    public void setAllSubjects(List<Subject> allSubjects) {
        this.allSubjects = allSubjects;
    }

    public Term getModTerm() {
        return modTerm;
    }

    public void setModTerm(Term modTerm) {
        this.modTerm = modTerm;
    }

    public Term getNewTerm() {
        return newTerm;
    }

    public void setNewTerm(Term newTerm) {
        this.newTerm = newTerm;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerm(List<Term> terms) {
        this.terms = terms;
    }

    public List<Term> getSelectedTerms() {
        return selectedTerms;
    }

    public void setSelectedTerms(List<Term> selectedTerms) {
        this.selectedTerms = selectedTerms;
    }

    public List<Subject> getTermSubjects() {
        return termSubjects;
    }

    public void setTermSubjects(List<Subject> termSubjects) {
        this.termSubjects = termSubjects;
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }


    public void loadAllTermsAndAllSubjects() {
        this.terms = termManager.getTerms();
        this.allSubjects = subjectManager.getSubjects();

        this.modTerm = (Term) getSession().getAttribute("modTerm");

        if (modTerm != null) {
            this.termSubjects = termManager.getSubjectsOfTerm(modTerm);
        }

        this.infoMessage = (String) getSession().getAttribute("infoMessage");

    }

    public String selectTerm() {
        modTerm = termManager.find(Integer.parseInt(modTermId));
        getSession().setAttribute("modTerm", this.modTerm);
        return "terms.xhtml?faces-redirect=true";
    }


    public String deleteTerm() {
        if (this.modTerm == null) {
            return "terms.xhtml?faces-redirect=true";
        }
        termManager.deleteSelectedTerm(this.modTerm);
        log.info("Log: Term deleted");
        return "terms.xhtml?faces-redirect=true";
    }

    public String addNewTerm() {

        for (Subject s : allSubjects) {
            if (s.isIsSelected()) {
                selectedSubjects.add(s);
            }
        }

        if (selectedSubjects.isEmpty()) {
            String infoMessage = "You must select at least one subject!";
            getSession().setAttribute("infoMessage", infoMessage);
            return "terms.xhtml?faces-redirect=true";
        }

        try {
            if (newTerm == null) {
                newTerm = new Term();
            }
            log.info("Log: newTerm " + newTerm);
            log.info("Log: selectedSubjects " + selectedSubjects);

            termManager.addTerm(newTerm, selectedSubjects);
        } catch (TermAlreadyExistsException e) {
            setInfoMessage(e.MESSAGE);
        }
        log.info("Log: Term " + newTerm + " added.");
        getSession().setAttribute("infoMessage", null);
        return "terms.xhtml?faces-redirect=true";
    }

    public String modifyTerm() {
        for (Subject s : allSubjects) {
            if (s.isIsSelected()) {
                selectedSubjects.add(s);
            }
        }

        if (selectedSubjects.isEmpty()) {
            String infoMessage = "You must select at least one subject!";
            getSession().setAttribute("infoMessage", infoMessage);
            return "terms.xhtml?faces-redirect=true";
        }

        Term termToMod = (Term) getSession().getAttribute("modTerm");
        log.info("Log: termToMod got: "+termToMod);

        Term termToModify = termManager.find(termToMod.getId());
        termToModify.setDuration(newTerm.getDuration());

        try {
            termManager.addTerm(termToModify, selectedSubjects);
        } catch (TermAlreadyExistsException e) {
            setInfoMessage(e.MESSAGE);
        }

        log.info("Log: Term " + termToModify + " modified.");
        getSession().setAttribute("infoMessage", null);
        return "terms.xhtml?faces-redirect=true";
    }




    public String check() {
        return (String) getSession().getAttribute("addOrMod");
    }


    public String toModTerm() {
        if (this.modTerm==null) {
            return "terms.xhtml?faces-redirect=true";
        }
        getSession().setAttribute("addOrMod", "mod");
        getSession().setAttribute("allSubjects", this.allSubjects);
        getSession().setAttribute("modTerm", this.modTerm);
        return "addModifyTerm.xhtml?faces-redirect=true";
    }

    public String toAddTerm() {
        getSession().setAttribute("addOrMod", "add");
        getSession().setAttribute("allSubjects", this.allSubjects);
        getSession().setAttribute("modTerm", this.modTerm);
        return "addModifyTerm.xhtml?faces-redirect=true";
    }



    @PostConstruct
    public void setUp () {
        loadAllTermsAndAllSubjects();
    }
}
