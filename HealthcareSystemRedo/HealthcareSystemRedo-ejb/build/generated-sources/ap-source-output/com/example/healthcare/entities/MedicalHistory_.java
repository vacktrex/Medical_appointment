package com.example.healthcare.entities;

import com.example.healthcare.entities.Users;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-06-13T16:50:05")
@StaticMetamodel(MedicalHistory.class)
public class MedicalHistory_ { 

    public static volatile SingularAttribute<MedicalHistory, Date> date;
    public static volatile SingularAttribute<MedicalHistory, String> symptoms;
    public static volatile SingularAttribute<MedicalHistory, String> notes;
    public static volatile SingularAttribute<MedicalHistory, Users> doctorId;
    public static volatile SingularAttribute<MedicalHistory, Users> patientId;
    public static volatile SingularAttribute<MedicalHistory, Integer> historyId;
    public static volatile SingularAttribute<MedicalHistory, String> diagnosis;

}