package com.peters.Epay.product.service;

import com.peters.Epay.product.dto.CartResponse;
import com.peters.Epay.product.dto.ProductRequestDto;
import com.peters.Epay.user.dto.CustomResponse;
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

    ResponseEntity<CustomResponse> getOrders();
}
