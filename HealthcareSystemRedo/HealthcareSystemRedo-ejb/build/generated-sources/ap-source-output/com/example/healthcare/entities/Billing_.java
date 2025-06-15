package com.example.healthcare.entities;

import com.example.healthcare.entities.Users;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2025-06-13T16:50:05")
@StaticMetamodel(Billing.class)
public class Billing_ { 

    public static volatile SingularAttribute<Billing, Date> dateIssued;
    public static volatile SingularAttribute<Billing, BigDecimal> amount;
    public static volatile SingularAttribute<Billing, Users> doctorId;
    public static volatile SingularAttribute<Billing, Users> patientId;
    public static volatile SingularAttribute<Billing, Integer> billId;
    public static volatile SingularAttribute<Billing, String> description;
    public static volatile SingularAttribute<Billing, String> status;

}