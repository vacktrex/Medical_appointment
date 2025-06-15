/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.healthcare.sessionbeans;

import com.example.healthcare.entities.Billing;
import com.example.healthcare.entities.Users;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author user
 */
@Stateless
public class BillingFacade extends AbstractFacade<Billing> {

    @PersistenceContext(unitName = "HealthcareSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BillingFacade() {
        super(Billing.class);
    }
    public int countUnpaidByPatient(Users patient) {
        return ((Long) em.createQuery(
            "SELECT COUNT(b) FROM Billing b WHERE b.patientId = :patientId AND b.status = 'unpaid'")
            .setParameter("patientId", patient.getUserId())  // ✅ This now works with .userId
            .getSingleResult()).intValue();
    }

    public BigDecimal sumPaidByPatient(Users patient) {
        return (BigDecimal) em.createQuery(
            "SELECT COALESCE(SUM(b.amount), 0) FROM Billing b " +
            "WHERE b.patientId = :patientId AND b.status = 'paid'")
            .setParameter("patientId", patient.getUserId())  // ✅ Same fix here
            .getSingleResult();
    }




    public BigDecimal sumEarningsForDoctorInMonth(Long doctorId, LocalDate date) {
        try {
            LocalDate start = date.withDayOfMonth(1);
            LocalDate end = start.plusMonths(1).minusDays(1);

            System.out.println("DEBUG: doctorId=" + doctorId + " start=" + start + " end=" + end);

            return (BigDecimal) em.createQuery(
                "SELECT COALESCE(SUM(b.amount), 0) FROM Billing b " +
                "WHERE b.doctorId.userId = :doctorId " + 
                "AND b.status = 'paid' " +
                "AND b.dateIssued BETWEEN :start AND :end")
                .setParameter("doctorId", doctorId)
                .setParameter("start", java.sql.Date.valueOf(start))
                .setParameter("end", java.sql.Date.valueOf(end))
                .getSingleResult();

        } catch (Exception e) {
            System.err.println("ERROR in sumEarningsForDoctorInMonth: " + e.getMessage());
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
} 
