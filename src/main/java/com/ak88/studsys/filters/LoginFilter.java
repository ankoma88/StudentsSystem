package com.ak88.studsys.filters;

import com.ak88.studsys.controller.LoginController;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        LoginController session = (LoginController) req.getSession().getAttribute("loginBean");
        String url = req.getRequestURI();

        if (session == null || !session.isLogged()) {
            if (url.contains("titlePage.xhtml") || url.contains("logout.xhtml")) {
                resp.sendRedirect(req.getServletContext().getContextPath() + "/login.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        } else {
            if (url.contains("register.xhtml") || url.contains("login.xhtml")){
                resp.sendRedirect(req.getServletContext().getContextPath() + "/titlePage.xhtml");
            }
            else if (url.contains("logout.xhtml")) {

                req.getSession().removeAttribute("loginBean");
                req.getSession().invalidate();
                resp.sendRedirect(req.getServletContext().getContextPath() + "/login.xhtml");
            }
            else {
                chain.doFilter(request, response);
            }
        }
    }


    @Override
    public void destroy() {

    }
}
