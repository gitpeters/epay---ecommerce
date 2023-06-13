package com.peters.User_Registration_and_Email_Verification.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponse {
    private String productName;
    private int unit;
    private double amount;
    private double subtotal;
    private String status;
    private String message;


    public CartResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
