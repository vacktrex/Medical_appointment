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
@Table(name = "APPOINTMENTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Appointments.findAll", query = "SELECT a FROM Appointments a"),
    @NamedQuery(name = "Appointments.findByAppointmentId", query = "SELECT a FROM Appointments a WHERE a.appointmentId = :appointmentId"),
    @NamedQuery(name = "Appointments.findByDateTime", query = "SELECT a FROM Appointments a WHERE a.dateTime = :dateTime"),
    @NamedQuery(name = "Appointments.findByReason", query = "SELECT a FROM Appointments a WHERE a.reason = :reason"),
    @NamedQuery(name = "Appointments.findByStatus", query = "SELECT a FROM Appointments a WHERE a.status = :status")})
public class Appointments implements Serializable {

    @Column(name = "DATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    @Size(max = 255)
    @Column(name = "REASON")
    private String reason;
    @Size(max = 255)
    @Column(name = "STATUS")
    private String status;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "APPOINTMENT_ID")
    private Integer appointmentId;
    @JoinColumn(name = "DOCTOR_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Users doctorId;
    @JoinColumn(name = "PATIENT_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Users patientId;

    public Appointments() {
    }

    public Appointments(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Appointments(Integer appointmentId, Date dateTime) {
        this.appointmentId = appointmentId;
        this.dateTime = dateTime;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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
        hash += (appointmentId != null ? appointmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Appointments)) {
            return false;
        }
        Appointments other = (Appointments) object;
        if ((this.appointmentId == null && other.appointmentId != null) || (this.appointmentId != null && !this.appointmentId.equals(other.appointmentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.healthcare.entities.Appointments[ appointmentId=" + appointmentId + " ]";
    }



    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
        
}
