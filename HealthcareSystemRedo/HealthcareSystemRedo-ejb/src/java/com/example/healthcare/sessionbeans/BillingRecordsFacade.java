package com.example.healthcare.sessionbeans;

import com.example.healthcare.entities.Billing;
import com.example.healthcare.entities.PatientProfiles;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class BillingRecordsFacade extends AbstractFacade<Billing> {

    @PersistenceContext(unitName = "HealthcareSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BillingRecordsFacade() {
        super(Billing.class);
    }

    /**
     * Finds and returns a list of billing records for a specific patient profile.
     * Records are ordered by dateIssued in descending order.
     * @param patientProfile The PatientProfiles entity for which to find records.
     * @return A List of Billing entities for the given patient, or an empty list if none found.
     */
    public List<Billing> findByPatientProfile(PatientProfiles patientProfile) {
        // Corrected: Billing.patientId is a Users entity.
        // PatientProfiles.users is also a Users entity.
        // We need to match the 'Users' entity directly for the patient.
        TypedQuery<Billing> query = em.createQuery(
            "SELECT b FROM Billing b WHERE b.patientId = :patientUser ORDER BY b.dateIssued DESC", Billing.class);
        query.setParameter("patientUser", patientProfile.getUsers()); // Pass the actual Users object
        return query.getResultList();
    }
}