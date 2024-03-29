package com.peters.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
