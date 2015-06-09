package com.ak88.studsys.controller;

import com.ak88.studsys.model.Student;
import com.ak88.studsys.model.Subject;
import com.ak88.studsys.model.Term;
import com.ak88.studsys.service.interfaces.ProgressManagerLocal;
import com.ak88.studsys.service.interfaces.StudentManagerLocal;
import com.ak88.studsys.service.interfaces.SubjectManagerLocal;
import com.ak88.studsys.service.interfaces.TermManagerLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Named
@RequestScoped
public class ShowProgressBean extends BaseController implements Serializable {
    static final Logger log = LoggerFactory.getLogger(ShowProgressBean.class);


    @EJB
    private StudentManagerLocal studentManager;

    @EJB
    private SubjectManagerLocal subjectManager;

    @EJB
    private TermManagerLocal termManager;

    @EJB
    private ProgressManagerLocal progressManager;

    String modTermId;
    Term modTerm = new Term();
    private List<Term> terms;

    private Student selStudent;

    private Map<Subject, Integer> subjectMarkMap = new HashMap<>();

    private Integer averageScore;

    public List<Term> getTerms() {
        return terms;
    }


    public Term getModTerm() {
        return modTerm;
    }

    public void setModTerm(Term modTerm) {
        this.modTerm = modTerm;
    }

    public String getModTermId() {
        return modTermId;
    }

    public void setModTermId(String modTermId) {
        this.modTermId = modTermId;
    }


    public List<Map.Entry<Subject, Integer>> getSubjects() {

        if (subjectMarkMap != null) {
            Set<Map.Entry<Subject, Integer>> subjectsSet = subjectMarkMap.entrySet();
            return new ArrayList<>(subjectsSet);
        } else {
            return null;
        }
    }


    public Map<Subject, Integer> getSubjectMarkMap() {
        return subjectMarkMap;
    }

    public void setSubjectMarkMap(Map<Subject, Integer> subjectMarkMap) {
        this.subjectMarkMap = subjectMarkMap;
    }

    public Student getSelStudent() {
        return selStudent;
    }

    public void setSelStudent(Student selStudent) {
        this.selStudent = selStudent;
    }

    public Integer getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Integer averageScore) {
        this.averageScore = averageScore;
    }



    public String selectTerm() {

        modTerm = termManager.find(Integer.parseInt(modTermId));
        log.info("Log: selStudent: " + selStudent);
        log.info("Log: modTerm: " + modTerm);
        subjectMarkMap = progressManager.loadProgress(selStudent, modTerm);
        log.info("Log: subjectMarkMap: " + subjectMarkMap.size());
        this.averageScore = calcAverageScore();
        log.info("Log: averageScore: " + averageScore);
        getSession().setAttribute("subjectMarkMap", subjectMarkMap);
        getSession().setAttribute("averageScore", averageScore);
        getSession().setAttribute("modTerm", modTerm);
        return "progress.xhtml?faces-redirect=true";
    }

    private int calcAverageScore() {
        List<Integer> marks = new ArrayList<>(subjectMarkMap.values());
        log.info("Log: marks: " + marks);
        Integer sum = 0;
        for (Integer mark : marks) {
            sum += mark;
        }
        return sum/marks.size();
    }

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(date);
    }

    public void createRandomProgress() {
        progressManager.instantiateProgresses(); //load random marks to database
    }

    @PostConstruct
    public void setUp () {
        subjectMarkMap = (Map<Subject, Integer>) getSession().getAttribute("subjectMarkMap");
        if (subjectMarkMap == null) {
            subjectMarkMap = new HashMap<>();
        }
        selStudent = (Student) getSession().getAttribute("studentToMod");
        modTerm = (Term) getSession().getAttribute("modTerm");
        averageScore = (Integer) getSession().getAttribute("averageScore");
        terms = termManager.getTerms();
    }
}
