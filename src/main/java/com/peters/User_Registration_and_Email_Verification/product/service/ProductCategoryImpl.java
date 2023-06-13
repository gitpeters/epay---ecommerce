package com.peters.User_Registration_and_Email_Verification.product.service;

import com.peters.User_Registration_and_Email_Verification.product.dto.ProductCategoryRequest;
import com.peters.User_Registration_and_Email_Verification.product.entity.Product;
import com.peters.User_Registration_and_Email_Verification.product.entity.ProductCategory;
import com.peters.User_Registration_and_Email_Verification.exception.CategoryAlreadyExistsException;
import com.peters.User_Registration_and_Email_Verification.product.repository.IProductRepository;
import com.peters.User_Registration_and_Email_Verification.product.repository.ProductCategoryRepository;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductCategoryImpl implements ICategoryService{
    private final IProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    @Override
    public List<ProductCategory> getAllCategories() {

        return categoryRepository.findAll();
    }

    @Override
    public ProductCategory createCategory(ProductCategoryRequest request) {
        Optional<ProductCategory> categoryOpt = categoryRepository.findByName(request.getCategoryName());
        if(categoryOpt.isPresent()){
            throw new CategoryAlreadyExistsException("Category already exist");
        }
        return categoryRepository.save(ProductCategory.builder()
                .name(request.getCategoryName()).build());
    }

    @Override
    public void deleteCategory(Long categoryId) {
        this.removeAllProductFromCategory(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public ProductCategory findByName(String name) {
        return categoryRepository.findByName(name).get();
    }

    @Override
    public ProductCategory findById(Long categoryId) {
        return categoryRepository.findById(categoryId).get();
    }

    @Override
    public ResponseEntity<CustomResponse> removeProductFromCategory(Long productId, Long categoryId) {
        Optional<ProductCategory> category = categoryRepository.findById(categoryId);
        Optional<Product> product = productRepository.findById(productId);
        if(category.isPresent() && category.get().getProducts().contains(product.get())){
            category.get().removeProductFromCategory(product.get());
            categoryRepository.save(category.get());
            return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, product.get(), "Successfully removed product from category"));
        }
        return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.NOT_FOUND, "No product found"));
    }

    @Override
    public ResponseEntity<CustomResponse> assignProductToCategory(Long productId, Long categoryId) {
        Optional<ProductCategory> category = categoryRepository.findById(categoryId);
        Optional<Product> product = productRepository.findById(productId);
        if(product.isPresent() && product.get().getCategory().contains(category.get())){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.CONFLICT, product.get().getName()+" has already been to assigned to "+category.get().getName()+" category"));
        }
        category.ifPresent(assignRole -> assignRole.assignProductToCategory(product.get()));
        categoryRepository.save(category.get());
        return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, category.get(), "Successful"));
    }

    @Override
    public ProductCategory removeAllProductFromCategory(Long categoryId) {
        Optional<ProductCategory> category = categoryRepository.findById(categoryId);
        category.ifPresent(ProductCategory::removeAllProductsFromCategory);
        return categoryRepository.save(category.get());
    }
}
