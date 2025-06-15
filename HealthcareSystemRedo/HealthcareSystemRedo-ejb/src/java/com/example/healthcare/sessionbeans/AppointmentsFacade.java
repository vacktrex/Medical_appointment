package com.example.healthcare.sessionbeans;

import com.example.healthcare.entities.Appointments;
import com.example.healthcare.entities.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


@Stateless
public class AppointmentsFacade extends AbstractFacade<Appointments> {

    // IMPORTANT: Ensure this matches the name of your persistence unit (found in persistence.xml)
    @PersistenceContext(unitName = "HealthcareSystemPU") // <-- VERIFY THIS NAME!
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AppointmentsFacade() {
        super(Appointments.class);
    }

    /**
     * Finds and returns a list of appointments for a specific doctor.
     * Appointments are ordered by date and time in ascending order.
     * @param doctor The Users entity representing the doctor.
     * @return A List of Appointments for the given doctor, or an empty list if none found.
     */
    public List<Appointments> findByDoctor(Users doctor) {
        if (doctor == null) {
            return new java.util.ArrayList<>();
        }
        TypedQuery<Appointments> query = em.createQuery(
            "SELECT a FROM Appointments a WHERE a.doctorId = :doctor ORDER BY a.dateTime ASC", Appointments.class);
        query.setParameter("doctor", doctor);
        return query.getResultList();
    }

    /**
     * Finds all appointments associated with a specific patient.
     * Appointments are ordered by date and time in descending order.
     *
     * @param patient The Users entity representing the patient.
     * @return A list of Appointments for the given patient, or an empty list if none found.
     */
    public List<Appointments> findByPatient(Users patient) {
        if (patient == null) {
            return new java.util.ArrayList<>();
        }
        TypedQuery<Appointments> query = em.createQuery(
            "SELECT a FROM Appointments a WHERE a.patientId = :patient ORDER BY a.dateTime DESC", Appointments.class);
        query.setParameter("patient", patient);
        return query.getResultList();
    }

    public int countUpcomingByDoctor(Long doctorId) {
        if (doctorId == null) {
            return 0;
        }

        Long count = (Long) em.createQuery(
            "SELECT COUNT(a) FROM Appointments a " +
            "WHERE a.doctorId.userId = :doctorId " +
            "AND a.dateTime >= CURRENT_TIMESTAMP " +
            "AND LOWER(a.status) = 'confirmed'")
            .setParameter("doctorId", doctorId)
            .getSingleResult();

        return count.intValue();
    }


    public int countPendingByDoctor(Long doctorId) {
        return ((Long) em.createQuery(
            "SELECT COUNT(a) FROM Appointments a WHERE a.doctorId.userId = :doctorId AND a.status = 'pending'")
            .setParameter("doctorId", doctorId)
            .getSingleResult()).intValue();
    }

    public int countByPatient(Users patient) {
    return ((Long) em.createQuery(
        "SELECT COUNT(a) FROM Appointments a WHERE a.patientId = :patient")
        .setParameter("patient", patient)
        .getSingleResult()).intValue();
    }

    public int countUpcomingByPatient(Users patient) {
        return ((Long) em.createQuery(
            "SELECT COUNT(a) FROM Appointments a WHERE a.patientId = :patient AND a.dateTime >= CURRENT_DATE AND a.status = 'confirmed'")
            .setParameter("patient", patient)
            .getSingleResult()).intValue();
    }
    /**
     * Finds appointments for a specific user (doctor or patient) by status.
     * Appointments are ordered by date and time in descending order.
     *
     * @param user The Users entity (either doctor or patient).
     * @param status The status of the appointment (e.g., "pending", "confirmed", "cancelled").
     * @param forDoctor True if the user is a doctor (filter by doctorId), false if a patient (filter by patientId).
     * @return A list of Appointments for the given user with the specified status.
     */
    public List<Appointments> findByUserAndStatus(Users patient, String status, boolean isDoctor) {
         String jpql = isDoctor
             ? "SELECT a FROM Appointments a WHERE a.doctorId = :user AND a.status = :status ORDER BY a.dateTime DESC"
             : "SELECT a FROM Appointments a WHERE a.patientId = :user AND a.status = :status ORDER BY a.dateTime DESC";

         return em.createQuery(jpql, Appointments.class)
                  .setParameter("user", patient)
                  .setParameter("status", status)
                  .getResultList();
     }

    
}