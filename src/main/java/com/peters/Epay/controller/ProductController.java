package com.peters.Epay.controller;

import com.peters.Epay.product.dto.ProductRequestDto;
import com.peters.Epay.product.entity.ProductCategory;
import com.peters.Epay.product.service.ICategoryService;
import com.peters.Epay.product.service.IProductService;
import com.peters.Epay.product.service.ProductImageService;
import com.peters.Epay.user.dto.CustomResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
@Tag(name = "product")
public class ProductController {
    private final IProductService productService;
    private final ProductImageService imageService;
    private final ICategoryService categoryService;

    @PostMapping("/{productId}/add-image")
    public ResponseEntity<CustomResponse> addProductImage(@RequestParam("product-image")MultipartFile file, @PathVariable("productId") Long productId) throws Exception {
        return imageService.addImageToProduct(file, productId);
    }

    @PostMapping("/add-product")
    public ResponseEntity<CustomResponse> addProduct(@RequestBody ProductRequestDto request, @RequestParam("userId") Long userId) {
        return productService.addProduct(request, userId);
    }

    @GetMapping
    public ResponseEntity<CustomResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping("/assign-category")
    public ResponseEntity<CustomResponse> assignCategoryToProduct(@RequestParam("productId") Long productId, @RequestParam("categoryId") Long categoryId){
        return categoryService.assignProductToCategory(productId, categoryId);
    }

    @GetMapping("/all-categories")
    public ResponseEntity<List<ProductCategory>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse> getProductsById(@PathVariable("id") Long productId){
        return productService.getProductById(productId);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<CustomResponse> editProduct(@PathVariable("productId") Long productId, @RequestBody ProductRequestDto requestDto){
        return productService.UpdateProduct(productId, requestDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CustomResponse> deleteProduct(@PathVariable("productId") Long productId){
        return productService.deleteProduct(productId);
    }


}
