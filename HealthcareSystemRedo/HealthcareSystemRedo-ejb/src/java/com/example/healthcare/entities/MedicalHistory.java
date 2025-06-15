/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.healthcare.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@Table(name = "MEDICAL_HISTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MedicalHistory.findAll", query = "SELECT m FROM MedicalHistory m"),
    @NamedQuery(name = "MedicalHistory.findByHistoryId", query = "SELECT m FROM MedicalHistory m WHERE m.historyId = :historyId"),
    @NamedQuery(name = "MedicalHistory.findByDate", query = "SELECT m FROM MedicalHistory m WHERE m.date = :date"),
    @NamedQuery(name = "MedicalHistory.findBySymptoms", query = "SELECT m FROM MedicalHistory m WHERE m.symptoms = :symptoms"),
    @NamedQuery(name = "MedicalHistory.findByDiagnosis", query = "SELECT m FROM MedicalHistory m WHERE m.diagnosis = :diagnosis"),
    @NamedQuery(name = "MedicalHistory.findByNotes", query = "SELECT m FROM MedicalHistory m WHERE m.notes = :notes")})
public class MedicalHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "HISTORY_ID")
    private Integer historyId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Size(max = 1000)
    @Column(name = "SYMPTOMS")
    private String symptoms;
    @Size(max = 1000)
    @Column(name = "DIAGNOSIS")
    private String diagnosis;
    @Size(max = 2000)
    @Column(name = "NOTES")
    private String notes;
    @JoinColumn(name = "DOCTOR_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Users doctorId;
    @JoinColumn(name = "PATIENT_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Users patientId;

    public MedicalHistory() {
    }

    public MedicalHistory(Integer historyId) {
        this.historyId = historyId;
    }

    public MedicalHistory(Integer historyId, Date date) {
        this.historyId = historyId;
        this.date = date;
    }

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Users getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Users doctorId) {
        this.doctorId = doctorId;
    }

    public Users getPatientId() {
        return patientId;
    }

    public void setPatientId(Users patientId) {
        this.patientId = patientId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (historyId != null ? historyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedicalHistory)) {
            return false;
        }
        MedicalHistory other = (MedicalHistory) object;
        if ((this.historyId == null && other.historyId != null) || (this.historyId != null && !this.historyId.equals(other.historyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.healthcare.entities.MedicalHistory[ historyId=" + historyId + " ]";
    }
    
}
