package com.peters.User_Registration_and_Email_Verification.product.service;

import com.peters.User_Registration_and_Email_Verification.product.dto.ProductRequestDto;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface IProductService {
    ResponseEntity<CustomResponse> getAllProducts();

    ResponseEntity<CustomResponse> addProduct(ProductRequestDto product, Long userId);

    ResponseEntity<CustomResponse> getProductByName(String name);

    ResponseEntity<CustomResponse> getProductByCategory(String category);

    ResponseEntity<CustomResponse> getProductByPriceRange(Double min, Double max);

    ResponseEntity<CustomResponse> deleteProduct(Long productId);

    ResponseEntity<CustomResponse> UpdateProduct(Long productId, ProductRequestDto product);

}
