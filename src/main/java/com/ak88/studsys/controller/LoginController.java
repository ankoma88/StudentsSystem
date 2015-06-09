package com.ak88.studsys.controller;

import com.ak88.studsys.exceptions.UserNotFoundException;
import com.ak88.studsys.model.AppUser;
import com.ak88.studsys.service.interfaces.UserManagerLocal;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

//
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginController extends BaseController implements Serializable {

    @EJB
    private UserManagerLocal userManager;

    private String infoMessage;

    private AppUser currentUser = new AppUser();

    private String name;
    private String pass;
    private boolean isLogged = false;

    public LoginController() {
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public AppUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(AppUser currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isIsLogged() {
        return isLogged;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public String login() {

        try {
            currentUser = userManager.getUser(name, pass);
        } catch (UserNotFoundException e) {
            setInfoMessage(e.MESSAGE);
            return "login.xhtml";
        }
        isLogged = true;
        return "titlePage.xhtml?faces-redirect=true";
    }

}
