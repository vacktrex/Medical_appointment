package com.example.healthcare.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@Table(name = "PATIENT_PROFILES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PatientProfiles.findAll", query = "SELECT p FROM PatientProfiles p"),
    @NamedQuery(name = "PatientProfiles.findByPatientId", query = "SELECT p FROM PatientProfiles p WHERE p.patientId = :patientId"),
    @NamedQuery(name = "PatientProfiles.findByAge", query = "SELECT p FROM PatientProfiles p WHERE p.age = :age"),
    @NamedQuery(name = "PatientProfiles.findByGender", query = "SELECT p FROM PatientProfiles p WHERE p.gender = :gender"),
    @NamedQuery(name = "PatientProfiles.findByContactInfo", query = "SELECT p FROM PatientProfiles p WHERE p.contactInfo = :contactInfo"),
    // ADD THIS NAMED QUERY FOR findByUserId
    @NamedQuery(name = "PatientProfiles.findByUserId", query = "SELECT p FROM PatientProfiles p WHERE p.patientId = :userId")
})
public class PatientProfiles implements Serializable {

    @Size(max = 255)
    @Column(name = "CONTACT_INFO")
    private String contactInfo;
    @Size(max = 255)
    @Column(name = "GENDER")
    private String gender;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PATIENT_ID")
    private Integer patientId;
    @Column(name = "AGE")
    private Integer age;
    @JoinColumn(name = "PATIENT_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Users users; // This is the relationship back to the Users entity

    public PatientProfiles() {
    }

    public PatientProfiles(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (patientId != null ? patientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PatientProfiles)) {
            return false;
        }
        PatientProfiles other = (PatientProfiles) object;
        if ((this.patientId == null && other.patientId != null) || (this.patientId != null && !this.patientId.equals(other.patientId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.healthcare.entities.PatientProfiles[ patientId=" + patientId + " ]";
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}