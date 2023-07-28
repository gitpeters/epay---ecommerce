package com.peters.Epay.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderMessageNotification {
    private String userFullName;
    private String userEmail;
    private String orderReference;
    private double totalAmount;
    private List<CartResponse> products;
}
