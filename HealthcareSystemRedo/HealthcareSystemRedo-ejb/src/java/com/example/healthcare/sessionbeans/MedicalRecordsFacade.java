package com.example.healthcare.sessionbeans;

import com.example.healthcare.entities.MedicalHistory; // Corrected to MedicalHistory entity
import com.example.healthcare.entities.Users; // Important: MedicalHistory uses Users for patientId and doctorId
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class MedicalRecordsFacade extends AbstractFacade<MedicalHistory> {

    @PersistenceContext(unitName = "HealthcareSystemPU") // Verify this matches your persistence.xml
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MedicalRecordsFacade() {
        super(MedicalHistory.class);
    }

    /**
     * Finds and returns a list of medical history records for a specific patient.
     * Records are ordered by date in descending order.
     * @param patientUser The Users entity representing the patient for whom to find records.
     * @return A List of MedicalHistory records for the given patient, or an empty list if none found.
     */
    public List<MedicalHistory> findByPatientUser(Users patientUser) {
        // Corrected query to use 'patientId' which is a Users entity in MedicalHistory
        TypedQuery<MedicalHistory> query = em.createQuery(
            "SELECT m FROM MedicalHistory m WHERE m.patientId = :patientUser ORDER BY m.date DESC", MedicalHistory.class);
        query.setParameter("patientUser", patientUser);
        return query.getResultList();
    }
}