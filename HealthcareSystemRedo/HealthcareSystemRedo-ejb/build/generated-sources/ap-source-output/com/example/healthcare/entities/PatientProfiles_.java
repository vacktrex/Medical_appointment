package com.example.healthcare.entities;

import com.example.healthcare.entities.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-06-13T16:50:05")
@StaticMetamodel(PatientProfiles.class)
public class PatientProfiles_ { 

    public static volatile SingularAttribute<PatientProfiles, String> contactInfo;
    public static volatile SingularAttribute<PatientProfiles, String> gender;
    public static volatile SingularAttribute<PatientProfiles, Integer> patientId;
    public static volatile SingularAttribute<PatientProfiles, Integer> age;
    public static volatile SingularAttribute<PatientProfiles, Users> users;

}