package com.example.healthcare.web.beans;

import com.example.healthcare.entities.Billing; // Make sure this entity exists
import com.example.healthcare.entities.PatientProfiles; // Make sure this entity exists
import com.example.healthcare.entities.Users; // Make sure this entity exists
import com.example.healthcare.sessionbeans.BillingRecordsFacade; // Make sure this EJB exists
import com.example.healthcare.sessionbeans.PatientProfilesFacade; // Make sure this EJB exists
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
public class PatientBillingRecordsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private BillingRecordsFacade billingRecordsFacade;

    @EJB
    private PatientProfilesFacade patientProfilesFacade;

    @Inject
    private LoginBean loginBean; // To get the currently logged-in user

    private Users loggedInPatientUser;
    private PatientProfiles loggedInPatientProfile;
    private List<Billing> billingRecords;

    @PostConstruct
    public void init() {
        if (loginBean != null && loginBean.getLoggedInUser() != null) {
            loggedInPatientUser = loginBean.getLoggedInUser();
            if (loggedInPatientUser.getRole() != null && loggedInPatientUser.getRole().equalsIgnoreCase("patient")) {
                loggedInPatientProfile = patientProfilesFacade.findByUser(loggedInPatientUser);
                if (loggedInPatientProfile != null) {
                    loadBillingRecords();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Profile Missing", "Your patient profile could not be found."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Access Denied", "You are not authorized to view this page."));
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("../login.xhtml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No user logged in. Please log in first."));
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../login.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadBillingRecords() {
        if (loggedInPatientProfile != null) {
            // Assuming BillingRecordsFacade has a method like findByPatientProfile(PatientProfiles patient)
            billingRecords = billingRecordsFacade.findByPatientProfile(loggedInPatientProfile);
            if (billingRecords.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "No billing records found for you."));
            }
        }
    }

    // --- Getters ---
    public List<Billing> getBillingRecords() {
        return billingRecords;
    }

    public Users getLoggedInPatientUser() {
        return loggedInPatientUser;
    }

    public PatientProfiles getLoggedInPatientProfile() {
        return loggedInPatientProfile;
    }
}