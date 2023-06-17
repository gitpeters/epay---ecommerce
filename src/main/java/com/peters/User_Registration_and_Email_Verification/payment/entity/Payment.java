//package com.peters.User_Registration_and_Email_Verification.payment.entity;
//
//import com.peters.User_Registration_and_Email_Verification.product.entity.ProductOrder;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.sql.Timestamp;
//import java.util.Calendar;
//
//@Data
//@Entity
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class Payment {
//    private Long id;
//    private double amount;
//    @OneToOne
//    @JoinColumn(name = "order_id")
//    private ProductOrder order;
//    private Timestamp createdOn;
//    private Timestamp lastModifiedOn;
//
//    @PrePersist
//    public void prePersist() {
//        createdOn = Timestamp.from(Calendar.getInstance().toInstant());
//        lastModifiedOn = Timestamp.from(Calendar.getInstance().toInstant());
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        lastModifiedOn = Timestamp.from(Calendar.getInstance().toInstant());
//    }
//}
//
//
