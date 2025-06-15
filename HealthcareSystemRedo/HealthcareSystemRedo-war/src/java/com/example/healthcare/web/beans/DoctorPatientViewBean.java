package com.example.healthcare.web.beans;

import com.example.healthcare.entities.MedicalHistory;
import com.example.healthcare.entities.PatientProfiles;
import com.example.healthcare.entities.Users;
import com.example.healthcare.sessionbeans.MedicalRecordsFacade;
import com.example.healthcare.sessionbeans.PatientProfilesFacade;
import com.example.healthcare.sessionbeans.UsersFacade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.faces.event.ComponentSystemEvent;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named
@ViewScoped
public class DoctorPatientViewBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UsersFacade userFacade;
    @EJB
    private PatientProfilesFacade patientProfilesFacade;
    @EJB
    private MedicalRecordsFacade medicalHistoryFacade;

    private List<PatientProfiles> allPatients;
    private PatientProfiles selectedPatient;
    private List<MedicalHistory> patientMedicalHistory;

    // Filtering properties
    private String searchKeyword;
    private String genderFilter;

    private Integer patientIdParam;

    private String newSymptoms;
    private String newDiagnosis;
    private String newNotes;
    private Date newDateIssued;

    @PostConstruct
    public void init() {
        loadAllPatients();
    }

    public void loadAllPatients() {
        allPatients = patientProfilesFacade.findAll();
    }

    public List<PatientProfiles> getFilteredPatients() {
        Stream<PatientProfiles> stream = allPatients.stream();

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            String keyword = searchKeyword.toLowerCase();
            stream = stream.filter(p -> p.getUsers().getUsername().toLowerCase().contains(keyword)
                    || p.getUsers().getFullName().toLowerCase().contains(keyword));
        }

        if (genderFilter != null && !genderFilter.trim().isEmpty()) {
            stream = stream.filter(p -> genderFilter.equalsIgnoreCase(p.getGender()));
        }

        return stream.collect(Collectors.toList());
    }

    public void loadPatientDetails(ComponentSystemEvent event) {
        if (patientIdParam != null) {
            selectedPatient = patientProfilesFacade.find(patientIdParam);
            if (selectedPatient != null) {
                loadPatientMedicalHistory();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Patient with ID " + patientIdParam + " not found."));
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("viewPatients.xhtml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No patient ID provided."));
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("viewPatients.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String viewPatientDetails() {
        return "patientDetails.xhtml?faces-redirect=true";
    }

    public void updatePatientProfile() {
        try {
            if (selectedPatient != null) {
                userFacade.edit(selectedPatient.getUsers());
                patientProfilesFacade.edit(selectedPatient);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Patient profile updated successfully."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No patient selected for update."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update patient profile: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    public void loadPatientMedicalHistory() {
        if (selectedPatient != null) {
            patientMedicalHistory = medicalHistoryFacade.findByPatientUser(selectedPatient.getUsers());
        }
    }

    public void addNewMedicalHistory() {
        try {
            if (selectedPatient == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No patient selected to add medical history."));
                return;
            }

            FacesContext context = FacesContext.getCurrentInstance();
            LoginBean login = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
            Users loggedInDoctor = login.getLoggedInUser();

            if (loggedInDoctor == null || !"doctor".equalsIgnoreCase(loggedInDoctor.getRole())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Only logged-in doctors can add medical history."));
                return;
            }

            MedicalHistory newRecord = new MedicalHistory();
            newRecord.setPatientId(selectedPatient.getUsers());
            newRecord.setDoctorId(loggedInDoctor);
            newRecord.setDate(newDateIssued != null ? newDateIssued : new Date());
            newRecord.setSymptoms(newSymptoms);
            newRecord.setDiagnosis(newDiagnosis);
            newRecord.setNotes(newNotes);

            medicalHistoryFacade.create(newRecord);
            loadPatientMedicalHistory();

            newSymptoms = null;
            newDiagnosis = null;
            newNotes = null;
            newDateIssued = null;

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "New medical history entry added successfully."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add medical history: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    // Getters and Setters

    public List<PatientProfiles> getAllPatients() {
        return allPatients;
    }

    public void setAllPatients(List<PatientProfiles> allPatients) {
        this.allPatients = allPatients;
    }

    public List<MedicalHistory> getPatientMedicalHistory() {
        return patientMedicalHistory;
    }

    public PatientProfiles getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(PatientProfiles selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    public String getNewSymptoms() {
        return newSymptoms;
    }

    public void setNewSymptoms(String newSymptoms) {
        this.newSymptoms = newSymptoms;
    }

    public String getNewDiagnosis() {
        return newDiagnosis;
    }

    public void setNewDiagnosis(String newDiagnosis) {
        this.newDiagnosis = newDiagnosis;
    }

    public String getNewNotes() {
        return newNotes;
    }

    public void setNewNotes(String newNotes) {
        this.newNotes = newNotes;
    }

    public Date getNewDateIssued() {
        return newDateIssued;
    }

    public void setNewDateIssued(Date newDateIssued) {
        this.newDateIssued = newDateIssued;
    }

    public Integer getPatientIdParam() {
        return patientIdParam;
    }

    public void setPatientIdParam(Integer patientIdParam) {
        this.patientIdParam = patientIdParam;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getGenderFilter() {
        return genderFilter;
    }

    public void setGenderFilter(String genderFilter) {
        this.genderFilter = genderFilter;
    }
}
