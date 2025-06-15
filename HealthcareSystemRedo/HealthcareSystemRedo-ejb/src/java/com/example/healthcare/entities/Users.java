/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.healthcare.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author user
 */
@Entity
@Table(name = "USERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByUserId", query = "SELECT u FROM Users u WHERE u.userId = :userId"),
    @NamedQuery(name = "Users.findByUsername", query = "SELECT u FROM Users u WHERE u.username = :username"),
    @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
    @NamedQuery(name = "Users.findByRole", query = "SELECT u FROM Users u WHERE u.role = :role"),
    @NamedQuery(name = "Users.findByFullName", query = "SELECT u FROM Users u WHERE u.fullName = :fullName"),
    @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email")})
public class Users implements Serializable {

    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 255)
    @Column(name = "FULL_NAME")
    private String fullName;
    @Size(max = 255)
    @Column(name = "PASSWORD")
    private String password;
    @Size(max = 255)
    @Column(name = "ROLE")
    private String role;
    @Size(max = 255)
    @Column(name = "USERNAME")
    private String username;

    @Size(max = 255)
    @Column(name = "CONTACT_INFO")
    private String contactInfo;

    @Size(max = 255)
    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "AGE")
    private Integer age;

    @Size(max = 50)
    @Column(name = "GENDER")
    private String gender;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USER_ID")
    private Integer userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctorId")
    private Collection<Appointments> appointmentsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patientId")
    private Collection<Appointments> appointmentsCollection1;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "users")
    private PatientProfiles patientProfiles;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctorId")
    private Collection<Billing> billingCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patientId")
    private Collection<Billing> billingCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctorId")
    private Collection<MedicalHistory> medicalHistoryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patientId")
    private Collection<MedicalHistory> medicalHistoryCollection1;

    public Users() {
    }

    public Users(Integer userId) {
        this.userId = userId;
    }

    public Users(Integer userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    @XmlTransient
    public Collection<Appointments> getAppointmentsCollection() {
        return appointmentsCollection;
    }

    public void setAppointmentsCollection(Collection<Appointments> appointmentsCollection) {
        this.appointmentsCollection = appointmentsCollection;
    }

    @XmlTransient
    public Collection<Appointments> getAppointmentsCollection1() {
        return appointmentsCollection1;
    }

    public void setAppointmentsCollection1(Collection<Appointments> appointmentsCollection1) {
        this.appointmentsCollection1 = appointmentsCollection1;
    }

    public PatientProfiles getPatientProfiles() {
        return patientProfiles;
    }

    public void setPatientProfiles(PatientProfiles patientProfiles) {
        this.patientProfiles = patientProfiles;
    }

    @XmlTransient
    public Collection<Billing> getBillingCollection() {
        return billingCollection;
    }

    public void setBillingCollection(Collection<Billing> billingCollection) {
        this.billingCollection = billingCollection;
    }

    @XmlTransient
    public Collection<Billing> getBillingCollection1() {
        return billingCollection1;
    }

    public void setBillingCollection1(Collection<Billing> billingCollection1) {
        this.billingCollection1 = billingCollection1;
    }

    @XmlTransient
    public Collection<MedicalHistory> getMedicalHistoryCollection() {
        return medicalHistoryCollection;
    }

    public void setMedicalHistoryCollection(Collection<MedicalHistory> medicalHistoryCollection) {
        this.medicalHistoryCollection = medicalHistoryCollection;
    }

    @XmlTransient
    public Collection<MedicalHistory> getMedicalHistoryCollection1() {
        return medicalHistoryCollection1;
    }

    public void setMedicalHistoryCollection1(Collection<MedicalHistory> medicalHistoryCollection1) {
        this.medicalHistoryCollection1 = medicalHistoryCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.healthcare.entities.Users[ userId=" + userId + " ]";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
