package com.example.healthcare.web.beans;

import com.example.healthcare.entities.Users;
import com.example.healthcare.sessionbeans.UsersFacade;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.inject.Inject;

@Named
@SessionScoped
public class PatientProfileBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UsersFacade usersFacade;

    @Inject
    private LoginBean loginBean;

    private Users user;

    @PostConstruct
    public void init() {
        if (loginBean != null && loginBean.getLoggedInUser() != null) {
            this.user = loginBean.getLoggedInUser();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No user is logged in."));
        }
    }

    public String updateProfile() {
        try {
            usersFacade.edit(user);
            loginBean.setLoggedInUser(usersFacade.find(user.getUserId())); // refresh user
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Profile updated successfully!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Could not update profile: " + e.getMessage()));
            e.printStackTrace();
        }
        return null;
    }

    // --- Getters ---
    public Users getUser() {
        if (user == null && loginBean != null && loginBean.getLoggedInUser() != null) {
            user = loginBean.getLoggedInUser();
        }
        return user;
    }


    public LoginBean getLoginBean() {
        return loginBean;
    }
}
