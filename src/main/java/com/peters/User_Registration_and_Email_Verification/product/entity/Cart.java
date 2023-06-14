package com.peters.User_Registration_and_Email_Verification.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private String productName;
    private int unit;
    private double amount;
    private double subtotal;
}
