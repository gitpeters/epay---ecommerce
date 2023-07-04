package com.peters.Epay.payment.entity;

import com.peters.Epay.product.entity.ProductOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private String status;
    private String paymentChannel;
    private String paymentReference;
    private String paymentAccessCode;
    @OneToOne
    @JoinColumn(name = "order_id")
    private ProductOrder order;
    private String transactionDate;
}


