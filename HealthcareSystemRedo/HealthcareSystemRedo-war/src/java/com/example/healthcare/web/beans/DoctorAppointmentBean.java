package com.example.healthcare.web.beans;

import com.example.healthcare.entities.Appointments;
import com.example.healthcare.entities.Users;
import com.example.healthcare.sessionbeans.AppointmentsFacade;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named
@ViewScoped
public class DoctorAppointmentBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AppointmentsFacade appointmentsFacade;

    @Inject
    private LoginBean loginBean;

    private Users loggedInDoctor;

    private List<Appointments> pendingAppointmentRequests;
    private List<Appointments> confirmedAppointments;
    private List<Appointments> allDoctorAppointments; 
    private List<Appointments> filteredAppointments;

    private Appointments selectedAppointment;

    private String searchKeyword;
    private String statusFilter;

    @PostConstruct
    public void init() {
        if (loginBean != null && loginBean.getLoggedInUser() != null) {
            loggedInDoctor = loginBean.getLoggedInUser();
            if (loggedInDoctor.getRole() != null && loggedInDoctor.getRole().equalsIgnoreCase("doctor")) {
                loadDoctorAppointments();
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

    public void loadDoctorAppointments() {
        if (loggedInDoctor != null) {
            pendingAppointmentRequests = appointmentsFacade.findByUserAndStatus(loggedInDoctor, "pending", true);
            confirmedAppointments = appointmentsFacade.findByUserAndStatus(loggedInDoctor, "confirmed", true);
            allDoctorAppointments = appointmentsFacade.findByDoctor(loggedInDoctor);
            filteredAppointments = new ArrayList<>(allDoctorAppointments);

            if (pendingAppointmentRequests.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "You currently have no pending appointment requests."));
            }
        } else {
            pendingAppointmentRequests = new ArrayList<>();
            confirmedAppointments = new ArrayList<>();
            allDoctorAppointments = new ArrayList<>();
            filteredAppointments = new ArrayList<>();
        }
    }

    public void acceptAppointment(Appointments appointment) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (appointment != null && "pending".equalsIgnoreCase(appointment.getStatus())) {
                appointment.setStatus("confirmed");
                appointmentsFacade.edit(appointment);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Appointment with " + appointment.getPatientId().getFullName() + " confirmed."));
                loadDoctorAppointments();
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Appointment cannot be accepted (not found or not pending)."));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to accept appointment: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    public void declineAppointment(Appointments appointment) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (appointment != null && "pending".equalsIgnoreCase(appointment.getStatus())) {
                appointment.setStatus("declined");
                appointmentsFacade.edit(appointment);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Appointment with " + appointment.getPatientId().getFullName() + " declined."));
                loadDoctorAppointments();
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Appointment cannot be declined (not found or not pending)."));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to decline appointment: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    public void filterAppointments() {
        filteredAppointments = allDoctorAppointments.stream()
            .filter(appt -> {
                boolean matchesStatus = (statusFilter == null || statusFilter.isEmpty()) || appt.getStatus().equalsIgnoreCase(statusFilter);
                boolean matchesSearch = (searchKeyword == null || searchKeyword.trim().isEmpty()) ||
                        appt.getPatientId().getFullName().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                        (appt.getReason() != null && appt.getReason().toLowerCase().contains(searchKeyword.toLowerCase()));
                return matchesStatus && matchesSearch;
            })
            .collect(Collectors.toList());
    }

    public List<Appointments> getPendingAppointmentRequests() {
        return pendingAppointmentRequests;
    }

    public void setPendingAppointmentRequests(List<Appointments> pendingAppointmentRequests) {
        this.pendingAppointmentRequests = pendingAppointmentRequests;
    }

    public List<Appointments> getConfirmedAppointments() {
        return confirmedAppointments;
    }

    public void setConfirmedAppointments(List<Appointments> confirmedAppointments) {
        this.confirmedAppointments = confirmedAppointments;
    }

    public List<Appointments> getAllDoctorAppointments() {
        return allDoctorAppointments;
    }

    public void setAllDoctorAppointments(List<Appointments> allDoctorAppointments) {
        this.allDoctorAppointments = allDoctorAppointments;
    }

    public List<Appointments> getFilteredAppointments() {
        return filteredAppointments;
    }

    public Users getLoggedInDoctor() {
        return loggedInDoctor;
    }

    public Appointments getSelectedAppointment() {
        return selectedAppointment;
    }

    public void setSelectedAppointment(Appointments selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getStatusFilter() {
        return statusFilter;
    }

    public void setStatusFilter(String statusFilter) {
        this.statusFilter = statusFilter;
    }
}
