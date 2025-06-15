package com.example.healthcare.web.beans;

import com.example.healthcare.entities.MedicalHistory; // Corrected to MedicalHistory entity
import com.example.healthcare.entities.Users; // For loggedInPatientUser and patientId in MedicalHistory
import com.example.healthcare.sessionbeans.MedicalRecordsFacade; // Corrected to MedicalHistoryFacade
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class PatientMedicalHistoryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private MedicalRecordsFacade medicalHistoryFacade; // Corrected EJB to MedicalHistoryFacade

    @Inject
    private LoginBean loginBean; // To get the currently logged-in user

    private Users loggedInPatientUser;
    private List<MedicalHistory> medicalHistoryRecords; // Corrected to MedicalHistory list

    @PostConstruct
    public void init() {
        if (loginBean != null && loginBean.getLoggedInUser() != null) {
            loggedInPatientUser = loginBean.getLoggedInUser();
            // Basic role check: Ensure the logged-in user is actually a patient
            if (loggedInPatientUser.getRole() != null && loggedInPatientUser.getRole().equalsIgnoreCase("patient")) {
                loadMedicalHistory();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Access Denied", "You are not authorized to view this page."));
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("../login.xhtml"); // Redirect if not authorized
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No user logged in. Please log in first."));
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../login.xhtml"); // Redirect if no user is logged in
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadMedicalHistory() {
        if (loggedInPatientUser != null) {
            // Now using the Users entity directly as patientId in MedicalHistory is a Users object
            medicalHistoryRecords = medicalHistoryFacade.findByPatientUser(loggedInPatientUser);
            if (medicalHistoryRecords.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "No medical records found for you."));
            }
        }
    }

    // --- Getters ---
    public List<MedicalHistory> getMedicalHistoryRecords() {
        return medicalHistoryRecords;
    }

    public Users getLoggedInPatientUser() {
        return loggedInPatientUser;
    }
}