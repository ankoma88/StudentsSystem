package com.ak88.studsys.service;

import com.ak88.studsys.exceptions.UserAlreadyExistsException;
import com.ak88.studsys.exceptions.UserNotFoundException;
import com.ak88.studsys.model.AppUser;
import com.ak88.studsys.service.interfaces.UserManagerLocal;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


@Stateless
public class UserManager implements UserManagerLocal {

    @PersistenceContext(unitName = "studentsDB")
    public EntityManager em;


    @Override
    public AppUser getUser(String userName, String password) throws UserNotFoundException {
        Query query = em.createQuery("SELECT user FROM AppUser user WHERE user.loginName =:loginName AND user.password = :password");
        query.setParameter("loginName", userName);
        query.setParameter("password", password);
        AppUser user;
        try {
            user = (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException();
        }
        return user;
    }


    @Override
    public void addUser(AppUser newUser) throws UserAlreadyExistsException {
        Query query = em.createQuery("SELECT user FROM AppUser user WHERE user.loginName = :userName");
        query.setParameter("userName", newUser.getLoginName());
        try {
            query.getSingleResult();
            throw new UserAlreadyExistsException();
        } catch (NoResultException e) {
            em.persist(newUser);
            em.flush();
        }

    }


    @Override
    public List<AppUser> getUsers() {
        Query query = em.createQuery("SELECT user FROM AppUser user", AppUser.class);
        List<AppUser> result = query.getResultList();
        if (result == null) {
            return new ArrayList<>();
        }
        return result;

    }

    @Override
    public AppUser find(int id) {
        return em.find(AppUser.class, id);
    }


}
