package com.peters.User_Registration_and_Email_Verification.product.controller;

import com.peters.User_Registration_and_Email_Verification.product.dto.ProductCategoryRequest;
import com.peters.User_Registration_and_Email_Verification.product.entity.ProductCategory;
import com.peters.User_Registration_and_Email_Verification.product.service.ICategoryService;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminProductController {
    private final ICategoryService categoryService;

    @GetMapping("/all-categories")
    public ResponseEntity<List<ProductCategory>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/create-category")
    public ResponseEntity<ProductCategory> createCategory(@RequestBody ProductCategoryRequest request){
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @PostMapping("/remove-all-products-from-category/{id}")
    public ProductCategory removeAllProductFromCategory(@PathVariable("id") Long categoryId){
        return categoryService.removeAllProductFromCategory(categoryId);
    }

    @PostMapping("/remove-product-from-category")
    public ResponseEntity<CustomResponse> removeSingleUserFromRole(@RequestParam(name = "productId") Long productId, @RequestParam(name = "categoryId")Long categoryId){
        return categoryService.removeProductFromCategory(productId, categoryId);
    }


    @DeleteMapping("/delete-category/{id}")
    public void deleteRole (@PathVariable("id") Long categoryId){
        categoryService.deleteCategory(categoryId);
    }
}
