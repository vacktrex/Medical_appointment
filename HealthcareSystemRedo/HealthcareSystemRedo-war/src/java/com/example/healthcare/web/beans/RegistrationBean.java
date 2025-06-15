package com.example.healthcare.web.beans;

import com.example.healthcare.entities.Users;
import com.example.healthcare.sessionbeans.UsersFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped; // Or @RequestScoped
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named // Makes it accessible in JSF using its name (e.g., registerBean)
@SessionScoped // Keeps the bean in session across multiple requests, helpful for messages/validation
public class RegistrationBean implements Serializable {

    @EJB
    private UsersFacade userFacade; // Inject your EJB for database operations

    // Properties to bind to the registration form fields
    private String username;
    private String password;
    private String confirmPassword;
    private String role;
    private String fullName;
    private String email;

    // --- Getters and Setters ---
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // --- Registration Logic ---
    public String registerUser() {
        FacesContext context = FacesContext.getCurrentInstance();

        // 1. Basic Validation
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please fill in all required fields.", null));
            return null; // Stay on the same page
        }

        if (!password.equals(confirmPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password and Confirm Password do not match.", null));
            return null; // Stay on the same page
        }

        // 2. Check if username already exists
        try {
            Users existingUser = userFacade.findByUsername(username);
            if (existingUser != null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username already taken. Please choose a different one.", null));
                return null; // Stay on the same page
            }
        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error checking for existing user: " + e.getMessage());
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "An error occurred during registration. Please try again.", null));
            return null;
        }

        // 3. Create new Users entity and persist
        try {
            Users newUser = new Users();
            // User ID is auto-generated, no need to set
            newUser.setUsername(username);
            newUser.setPassword(password); // **IMPORTANT: In a real app, hash this password!**
            newUser.setRole(role);
            newUser.setFullName(fullName);
            newUser.setEmail(email);

            userFacade.create(newUser); // Use the EJB's create method

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration successful! You can now log in.", null));
            
            // Clear fields after successful registration
            resetFields();
            
            return "login.xhtml?faces-redirect=true"; // Redirect to login page
        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error during user registration: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for full error details
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "An error occurred during registration. Please try again.", null));
            return null; // Stay on the same page
        }
    }
    
    private void resetFields() {
        this.username = null;
        this.password = null;
        this.confirmPassword = null;
        this.role = null;
        this.fullName = null;
        this.email = null;
    }
}