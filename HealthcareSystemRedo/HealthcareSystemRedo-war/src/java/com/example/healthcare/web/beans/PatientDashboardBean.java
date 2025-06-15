package com.example.healthcare.web.beans;

import com.example.healthcare.entities.Users;
import com.example.healthcare.sessionbeans.AppointmentsFacade;
import com.example.healthcare.sessionbeans.BillingFacade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Named("patientDashboardBean")
@ViewScoped
public class PatientDashboardBean implements Serializable {

    private int totalAppointments;
    private int upcomingCount;
    private int unpaidBills;
    private BigDecimal totalPaid;

    @EJB
    private AppointmentsFacade appointmentsFacade;

    @EJB
    private BillingFacade billingFacade;

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    public void init() {
        try {
            Users patient = loginBean.getLoggedInUser();
            totalAppointments = appointmentsFacade.countByPatient(patient);
            upcomingCount = appointmentsFacade.countUpcomingByPatient(patient);
            unpaidBills = billingFacade.countUnpaidByPatient(patient);
            totalPaid = billingFacade.sumPaidByPatient(patient);
            
            System.out.println("Patient ID = " + patient.getUserId());
            System.out.println("Total appointments = " + totalAppointments);
            System.out.println("Upcoming = " + upcomingCount);
            System.out.println("Unpaid bills = " + unpaidBills);
            System.out.println("Total paid = " + totalPaid);

        } catch (Exception e) {
            System.err.println("ERROR in PatientDashboardBean.init(): " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public int getUpcomingCount() {
        return upcomingCount;
    }

    public int getUnpaidBills() {
        return unpaidBills;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid != null ? totalPaid : BigDecimal.ZERO;
    }
}
