package com.peters.User_Registration_and_Email_Verification.product.service;

import com.peters.User_Registration_and_Email_Verification.product.dto.ProductCategoryRequest;
import com.peters.User_Registration_and_Email_Verification.product.entity.ProductCategory;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICategoryService {
    List<ProductCategory> getAllCategories();

    ProductCategory createCategory(ProductCategoryRequest request);

    void deleteCategory(Long categoryId);

    ProductCategory findByName(String name);

    ProductCategory findById(Long categoryId);

    ResponseEntity<CustomResponse> removeProductFromCategory(Long productId, Long categoryId);

    ResponseEntity<CustomResponse> assignProductToCategory(Long productId, Long categoryId);

    ProductCategory removeAllProductFromCategory(Long categoryId);
}
