package com.example.healthcare.entities;

import java.io.Serializable;
import java.math.BigDecimal; // Make sure this import exists
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
import javax.validation.constraints.NotNull; // This might be from an old auto-gen, remove if not needed
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@Table(name = "BILLING")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Billing.findAll", query = "SELECT b FROM Billing b"),
    @NamedQuery(name = "Billing.findByBillId", query = "SELECT b FROM Billing b WHERE b.billId = :billId"),
    @NamedQuery(name = "Billing.findByAmount", query = "SELECT b FROM Billing b WHERE b.amount = :amount"),
    @NamedQuery(name = "Billing.findByDateIssued", query = "SELECT b FROM Billing b WHERE b.dateIssued = :dateIssued"),
    @NamedQuery(name = "Billing.findByStatus", query = "SELECT b FROM Billing b WHERE b.status = :status"),
    @NamedQuery(name = "Billing.findByDescription", query = "SELECT b FROM Billing b WHERE b.description = :description")})
public class Billing implements Serializable {

    @Basic(optional = false) // Add this if NetBeans didn't include it for NOT NULL columns
    @NotNull // If NetBeans added this, it's fine for NOT NULL DB columns
    @Column(name = "AMOUNT")
    private BigDecimal amount; // <--- CORRECTED TYPE TO BigDecimal
    @Basic(optional = false) // Add this if NetBeans didn't include it for NOT NULL columns
    @NotNull // If NetBeans added this, it's fine for NOT NULL DB columns
    @Column(name = "DATE_ISSUED")
    @Temporal(TemporalType.DATE)
    private Date dateIssued;
    @Size(max = 1000) // Changed to 1000 to match your DDL VARCHAR(1000)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 10) // Changed to 10 to match your DDL VARCHAR(10)
    @Column(name = "STATUS")
    private String status;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BILL_ID")
    private Integer billId;
    @JoinColumn(name = "DOCTOR_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Users doctorId;
    @JoinColumn(name = "PATIENT_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Users patientId;

    public Billing() {
    }

    public Billing(Integer billId) {
        this.billId = billId;
    }

    // Corrected constructor parameter and assignment
    public Billing(Integer billId, BigDecimal amount, Date dateIssued) {
        this.billId = billId;
        this.amount = amount;
        this.dateIssued = dateIssued;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    // Corrected getter and setter for amount
    public BigDecimal getAmount() { // <--- CORRECTED RETURN TYPE
        return amount;
    }

    public void setAmount(BigDecimal amount) { // <--- CORRECTED PARAMETER TYPE
        this.amount = amount;
    }

    public Date getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        hash += (billId != null ? billId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Billing)) {
            return false;
        }
        Billing other = (Billing) object;
        if ((this.billId == null && other.billId != null) || (this.billId != null && !this.billId.equals(other.billId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.healthcare.entities.Billing[ billId=" + billId + " ]";
    }

}