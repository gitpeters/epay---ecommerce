package com.peters.User_Registration_and_Email_Verification.product.entity;

import com.peters.User_Registration_and_Email_Verification.product.dto.CartResponse;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reference = UUID.randomUUID().toString();
    private double totalAmount;
    @Transient
    private List<CartResponse> products;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
