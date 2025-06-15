package com.example.healthcare.sessionbeans;

import com.example.healthcare.entities.PatientProfiles;
import com.example.healthcare.entities.Users; // Make sure to import the Users entity
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class PatientProfilesFacade extends AbstractFacade<PatientProfiles> {

    @PersistenceContext(unitName = "HealthcareSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PatientProfilesFacade() {
        super(PatientProfiles.class);
    }

    /**
     * Finds a PatientProfile based on the User's primary key (userId).
     * This uses the NamedQuery "PatientProfiles.findByUserId" which you have defined.
     * @param userId The primary key (Integer) of the Users entity.
     * @return The PatientProfiles entity, or null if not found.
     */
    public PatientProfiles findByUserId(Integer userId) {
        try {
            TypedQuery<PatientProfiles> query = em.createNamedQuery("PatientProfiles.findByUserId", PatientProfiles.class);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No PatientProfile found for userId: " + userId);
            return null;
        } catch (Exception e) {
            System.err.println("Error in PatientProfilesFacade.findByUserId: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public int countByDoctorId(Long doctorId) {
        return ((Long) em.createQuery(
            "SELECT COUNT(p) FROM PatientProfiles p WHERE p.assignedDoctor.userId = :doctorId")
            .setParameter("doctorId", doctorId)
            .getSingleResult()).intValue();
    }

    /**
     * Finds a PatientProfile based on the associated Users entity.
     * This uses the 'users' field in your PatientProfiles entity.
     * @param user The Users entity representing the patient.
     * @return The PatientProfiles entity, or null if not found.
     */
    public PatientProfiles findByUser(Users user) {
        try {
            // Corrected: Uses the 'users' field in PatientProfiles entity for the relationship
            TypedQuery<PatientProfiles> query = em.createQuery(
                "SELECT p FROM PatientProfiles p WHERE p.users = :user", PatientProfiles.class);
            query.setParameter("user", user);
            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No PatientProfile found for user: " + user.getUserId()); // Use getUserId() from Users
            return null;
        } catch (Exception e) {
            System.err.println("Error in PatientProfilesFacade.findByUser: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}