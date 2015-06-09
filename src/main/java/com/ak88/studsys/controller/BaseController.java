package com.ak88.studsys.controller;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//Utility class for access to session
public class BaseController {

    public static final String SYSTEM_ERROR = "System error ...";

    protected FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }

    protected HttpServletRequest getRequest() {
        return getRequest();
    }

    protected HttpSession getSession() {
        return (HttpSession) getContext().getExternalContext().getSession(false);
    }



}
