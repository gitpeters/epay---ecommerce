package com.peters.User_Registration_and_Email_Verification.product.dto;

import com.peters.User_Registration_and_Email_Verification.product.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductRequestDto {
    private String name;
    private String description;
    private Double price;
    private int unit;
}
