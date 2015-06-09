package com.ak88.studsys.model;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name= "APP_USER")
@Named
@RequestScoped
public class AppUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private int user_id;
    private String loginName;
    private String password;
    private boolean isAdmin;

    public AppUser() {
    }

    public AppUser(String loginName, String password, boolean isAdmin) {
        this.loginName = loginName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "USER_ID")
    public Integer getId() {
        return user_id;
    }

    public void setId(Integer id) {
        this.user_id = id;
    }

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "LOGIN_NAME")
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String userName) {
        this.loginName = userName;
    }

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "ISADMIN")
    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser)) return false;

        AppUser appUser = (AppUser) o;

        if (isAdmin != appUser.isAdmin) return false;
        if (!loginName.equals(appUser.loginName)) return false;
        if (!password.equals(appUser.password)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = loginName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (isAdmin ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return loginName;
    }
}
