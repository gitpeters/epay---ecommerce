package com.peters.User_Registration_and_Email_Verification.product.service;

import com.peters.User_Registration_and_Email_Verification.product.dto.CartResponse;
import com.peters.User_Registration_and_Email_Verification.product.dto.ProductRequestDto;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProductService {
    ResponseEntity<CustomResponse> getAllProducts();

    ResponseEntity<CustomResponse> addProduct(ProductRequestDto product, Long userId);

    ResponseEntity<CustomResponse> getProductByName(String name);

    ResponseEntity<CustomResponse> getProductById(Long productId);
    ResponseEntity<CustomResponse> getProductByCategory(String category);

    ResponseEntity<CustomResponse> getProductByPriceRange(Double min, Double max);

    ResponseEntity<CustomResponse> deleteProduct(Long productId);

    ResponseEntity<CustomResponse> UpdateProduct(Long productId, ProductRequestDto product);

    ResponseEntity<CartResponse> addProductToCart(Long productId, int unit);

    ResponseEntity<List<CartResponse>> getAllCarts();

    ResponseEntity<CartResponse> deleteProductFromCart(Long productId);

    ResponseEntity<CartResponse> clearCart();

    ResponseEntity<?> checkout(Long userId);

}
