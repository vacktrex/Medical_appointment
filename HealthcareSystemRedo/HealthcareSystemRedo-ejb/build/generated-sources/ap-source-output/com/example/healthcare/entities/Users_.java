package com.example.healthcare.entities;

import com.example.healthcare.entities.Appointments;
import com.example.healthcare.entities.Billing;
import com.example.healthcare.entities.MedicalHistory;
import com.example.healthcare.entities.PatientProfiles;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-06-13T16:50:05")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile CollectionAttribute<Users, Billing> billingCollection;
    public static volatile SingularAttribute<Users, String> role;
    public static volatile SingularAttribute<Users, String> address;
    public static volatile CollectionAttribute<Users, Appointments> appointmentsCollection1;
    public static volatile SingularAttribute<Users, PatientProfiles> patientProfiles;
    public static volatile SingularAttribute<Users, String> contactInfo;
    public static volatile SingularAttribute<Users, String> gender;
    public static volatile SingularAttribute<Users, String> fullName;
    public static volatile SingularAttribute<Users, Integer> userId;
    public static volatile SingularAttribute<Users, String> password;
    public static volatile CollectionAttribute<Users, MedicalHistory> medicalHistoryCollection;
    public static volatile CollectionAttribute<Users, MedicalHistory> medicalHistoryCollection1;
    public static volatile SingularAttribute<Users, String> email;
    public static volatile SingularAttribute<Users, Integer> age;
    public static volatile CollectionAttribute<Users, Billing> billingCollection1;
    public static volatile SingularAttribute<Users, String> username;
    public static volatile CollectionAttribute<Users, Appointments> appointmentsCollection;

}