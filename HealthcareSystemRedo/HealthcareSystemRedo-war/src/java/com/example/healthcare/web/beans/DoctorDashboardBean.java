package com.example.healthcare.web.beans;

import com.example.healthcare.entities.Users;
import com.example.healthcare.sessionbeans.AppointmentsFacade;
import com.example.healthcare.sessionbeans.BillingFacade;
import com.example.healthcare.sessionbeans.UsersFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;


@Named("dashboardBean")
@ViewScoped
public class DoctorDashboardBean implements Serializable {

    private int totalPatients;
    private int upcomingAppointments;
    private int pendingAppointments;
    private BigDecimal thisMonthEarnings;

    @Inject
    private LoginBean loginBean;
    
    @EJB
    private AppointmentsFacade appointmentsFacade;

    @EJB
    private BillingFacade billingFacade;

    @EJB
    private UsersFacade usersFacade;

    @PostConstruct
    public void init() {
        try {
            Users doctor = getLoggedInDoctor();
            if (doctor != null && doctor.getUserId() != null) {
                Long doctorId = doctor.getUserId().longValue();
                System.out.println("DoctorDashboardBean.init(): doctorId=" + doctorId);

                totalPatients = usersFacade.countAllPatients();
                System.out.println("Total patients: " + totalPatients);

                upcomingAppointments = appointmentsFacade.countUpcomingByDoctor(doctorId);
                System.out.println("Upcoming: " + upcomingAppointments);

                pendingAppointments = appointmentsFacade.countPendingByDoctor(doctorId);
                System.out.println("Pending: " + pendingAppointments);

                thisMonthEarnings = billingFacade.sumEarningsForDoctorInMonth(doctorId, LocalDate.now());
                System.out.println("Earnings: RM" + thisMonthEarnings);
            } else {
                System.out.println("Doctor not found in session.");
            }
        } catch (Exception e) {
            System.err.println("ERROR in DoctorDashboardBean.init(): " + e.getMessage());
            e.printStackTrace();
        }
    }





    private Users getLoggedInDoctor() {
        return loginBean.getLoggedInUser();
    }

    // Getters
    public int getTotalPatients() {
        return totalPatients;
    }

    public int getUpcomingAppointments() {
        return upcomingAppointments;
    }

    public int getPendingAppointments() {
        return pendingAppointments;
    }

    public BigDecimal getThisMonthEarnings() {
        return thisMonthEarnings != null ? thisMonthEarnings : BigDecimal.ZERO;
    }
}
