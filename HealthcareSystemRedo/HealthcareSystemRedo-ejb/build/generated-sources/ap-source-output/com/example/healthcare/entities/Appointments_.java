package com.example.healthcare.entities;

import com.example.healthcare.entities.Users;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-06-13T16:50:05")
@StaticMetamodel(Appointments.class)
public class Appointments_ { 

    public static volatile SingularAttribute<Appointments, Date> dateTime;
    public static volatile SingularAttribute<Appointments, String> reason;
    public static volatile SingularAttribute<Appointments, Users> doctorId;
    public static volatile SingularAttribute<Appointments, Users> patientId;
    public static volatile SingularAttribute<Appointments, Integer> appointmentId;
    public static volatile SingularAttribute<Appointments, String> status;

}