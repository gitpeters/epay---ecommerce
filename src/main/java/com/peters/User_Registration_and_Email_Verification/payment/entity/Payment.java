package com.peters.User_Registration_and_Email_Verification.payment.entity;

import com.peters.User_Registration_and_Email_Verification.product.entity.ProductOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Calendar;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private String status;
    private String paymentReference;
    private String paymentAccessCode;
    @OneToOne
    @JoinColumn(name = "order_id")
    private ProductOrder order;
    private Timestamp transactionDate;

    @PrePersist
    public void prePersist() {
        transactionDate = Timestamp.from(Calendar.getInstance().toInstant());
    }
}


