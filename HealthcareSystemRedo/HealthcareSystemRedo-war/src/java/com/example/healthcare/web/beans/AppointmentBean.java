package com.example.healthcare.web.beans;

import com.example.healthcare.entities.Appointments;
import com.example.healthcare.entities.Users;
import com.example.healthcare.sessionbeans.AppointmentsFacade;
import com.example.healthcare.sessionbeans.UsersFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Named("appointmentBean")
@ViewScoped
public class AppointmentBean implements Serializable {

    @EJB
    private AppointmentsFacade appointmentsFacade;
    @EJB
    private UsersFacade usersFacade;

    @Inject
    private LoginBean loginBean;

    // Form fields for requesting an appointment
    private Date dateTime;
    private String reason;
    private Integer selectedDoctorId;
    private String statusFilter;

    private List<Users> doctors;

    // NEW: List to hold the patient's appointments
    private List<Appointments> myAppointments;

    @PostConstruct
    public void init() {
        loadDoctors(); // Still load doctors for the request form
        loadMyAppointments(); // NEW: Load appointments for the logged-in patient
    }

    public void loadDoctors() {
        doctors = usersFacade.findUsersByRole("doctor");
    }

    /**
     * NEW METHOD: Loads appointments for the currently logged-in patient.
     */
    public void loadMyAppointments() {
        Users currentUser = loginBean.getLoggedInUser();
        if (currentUser != null && "patient".equalsIgnoreCase(currentUser.getRole())) {
            if (statusFilter == null || statusFilter.isEmpty()) {
                myAppointments = appointmentsFacade.findByPatient(currentUser);
            } else {
                myAppointments = appointmentsFacade.findByUserAndStatus(currentUser, statusFilter, false);
            }
        } else {
            myAppointments = new java.util.ArrayList<>();
        }
    }

    public void filterByStatus() {
        loadMyAppointments();
    }
    public String getStatusFilter() {
        return statusFilter;
    }

    public void setStatusFilter(String statusFilter) {
        this.statusFilter = statusFilter;
    }

    public String requestAppointment() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Users patient = loginBean.getLoggedInUser();
            if (patient == null || !"patient".equalsIgnoreCase(patient.getRole())) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Only logged-in patients can request appointments."));
                return null;
            }

            Users doctor = usersFacade.find(selectedDoctorId);
            if (doctor == null || !"doctor".equalsIgnoreCase(doctor.getRole())) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Selected doctor not found or invalid role."));
                return null;
            }

            // Validation for future date/time
            // Ensure dateTime is not null before calling .before()
            if (dateTime == null || dateTime.before(new Date())) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Date/Time", "Appointment must be in the future. Please select a valid date and time."));
                return null;
            }

            Appointments newAppointment = new Appointments();
            newAppointment.setPatientId(patient);
            newAppointment.setDoctorId(doctor);
            newAppointment.setDateTime(dateTime); // Set the single Date object
            newAppointment.setReason(reason);
            newAppointment.setStatus("pending");

            appointmentsFacade.create(newAppointment);

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Appointment Requested!", "Your appointment request has been submitted for " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateTime) + ". Status: Pending."));

            // Reset form fields
            this.dateTime = null; // Reset the single dateTime property
            this.reason = null;
            this.selectedDoctorId = null;

            // NEW: After successful request, refresh the list of appointments
            // This ensures the patient sees the newly requested appointment immediately if they navigate to the view page.
            loadMyAppointments();

            return "/patient/dashboard.xhtml?faces-redirect=true";
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Request Failed", "An error occurred: " + e.getMessage()));
            e.printStackTrace();
            return null;
        }
    }

    // --- Getters and Setters for form fields ---
    public Date getDateTime() { return dateTime; }
    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Integer getSelectedDoctorId() { return selectedDoctorId; }
    public void setSelectedDoctorId(Integer selectedDoctorId) { this.selectedDoctorId = selectedDoctorId; }

    public List<Users> getDoctors() { return doctors; }

    // NEW: Getter for the patient's appointments list
    public List<Appointments> getMyAppointments() {
        return myAppointments;
    }
    // No setter needed for myAppointments as it's populated by loadMyAppointments()
}