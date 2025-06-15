package com.example.healthcare.web.beans;

import com.example.healthcare.entities.Users;
import com.example.healthcare.sessionbeans.UsersFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UsersFacade userFacade;

    private String username;
    private String password;
    private Users loggedInUser;

    // --- Getters and Setters ---
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Users getLoggedInUser() { return loggedInUser; }
    public void setLoggedInUser(Users loggedInUser) { this.loggedInUser = loggedInUser; }
    public boolean isLoggedIn() { return loggedInUser != null; }
    public String getRole() { return loggedInUser != null ? loggedInUser.getRole() : null; }

    // --- Login Logic ---
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("--- LoginBean.login() called ---");
        System.out.println("Entered Username: " + username);
        System.out.println("Entered Password: " + password);
        System.out.println("Is userFacade null? " + (userFacade == null));

        try {
            Users user = userFacade.findByUsername(username);
            System.out.println("User found by findByUsername: " + (user == null ? "NULL" : user.getUsername()));

            if (user != null && verifyPassword(password, user.getPassword())) {
                this.loggedInUser = user;

                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login Successful!", "Welcome, " + user.getFullName() + "!"));

                if ("doctor".equalsIgnoreCase(user.getRole())) {
                    return "/doctor/dashboard.xhtml?faces-redirect=true"; // Explicit .xhtml and leading slash
                } else if ("patient".equalsIgnoreCase(user.getRole())) {
                    return "/patient/dashboard.xhtml?faces-redirect=true"; // Explicit .xhtml and leading slash
                } else {
                    // Handle unexpected role or default to a generic dashboard
                    return "/generalDashboard.xhtml?faces-redirect=true"; // Create this page if needed
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed", "Invalid username or password."));
                return null;
            }
        } catch (Exception e) {
            System.err.println("Login error: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace(); // Keep this for full stack trace debugging
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "An error occurred", "Please try again later."));
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        loggedInUser = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout Successful", "You have been logged out."));
        return "/login.xhtml?faces-redirect=true";
    }

    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return plainPassword.equals(hashedPassword);
    }
}