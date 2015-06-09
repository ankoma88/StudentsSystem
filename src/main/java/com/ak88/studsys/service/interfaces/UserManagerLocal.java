package com.ak88.studsys.service.interfaces;

import com.ak88.studsys.exceptions.UserAlreadyExistsException;
import com.ak88.studsys.exceptions.UserNotFoundException;
import com.ak88.studsys.model.AppUser;

import javax.ejb.Local;
import java.util.List;

@Local
public interface UserManagerLocal {

    public AppUser getUser(String name, String password) throws UserNotFoundException;
    public List<AppUser> getUsers();
    public void addUser(AppUser newUser) throws UserAlreadyExistsException;
    public AppUser find(int id);
}