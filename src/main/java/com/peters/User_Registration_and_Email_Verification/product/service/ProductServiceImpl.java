package com.peters.User_Registration_and_Email_Verification.product.service;

import com.peters.User_Registration_and_Email_Verification.product.dto.ProductRequestDto;
import com.peters.User_Registration_and_Email_Verification.product.dto.ProductResponseDto;
import com.peters.User_Registration_and_Email_Verification.product.entity.Product;
import com.peters.User_Registration_and_Email_Verification.product.entity.ProductCategory;
import com.peters.User_Registration_and_Email_Verification.product.entity.ProductImage;
import com.peters.User_Registration_and_Email_Verification.product.repository.IProductRepository;
import com.peters.User_Registration_and_Email_Verification.product.repository.ProductCategoryRepository;
import com.peters.User_Registration_and_Email_Verification.product.repository.ProductImageRepository;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.user.exception.DuplicateException;
import com.peters.User_Registration_and_Email_Verification.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService{
    private final IProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ProductImageRepository imageRepository;
    private final IUserRepository userRepository;
    private final ICategoryService categoryService;
    private final ProductImageService productImageService;

    @Override
    public ResponseEntity<CustomResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> responseDtoList=  products.stream().map((this::mappedToProductResponse)).collect(Collectors.toList());
        return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, Arrays.asList(responseDtoList), "Successful"));
    }



     @Override
    public ResponseEntity<CustomResponse> addProduct(ProductRequestDto request, Long userId) {
        Optional<Product> productOpt = productRepository.findByName(request.getName());
        Optional<UserEntity> user = userRepository.findById(userId);

        if (user.isPresent() && productOpt.isPresent() && user.get().getProducts().contains(productOpt.get())) {
            throw new DuplicateException("Product with the same name already exists for the user");
            //return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.CONFLICT, "Product with the same name already exists for the user"));
        }

        ProductCategory category = categoryRepository.findByName("Others")
                .orElseGet(() -> {
                    // Create a new "Others" category if it doesn't exist
                    ProductCategory newCategory = ProductCategory.builder()
                            .name("Others")
                            .build();
                    return categoryRepository.save(newCategory);
                });

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .category(Collections.singleton(category))
                .unit(request.getUnit())
                .build();

        productRepository.save(product);
        return ResponseEntity.ok(new CustomResponse(HttpStatus.CREATED, "Successful"));
    }


    @Override
    public ResponseEntity<CustomResponse> getProductByName(String name){
            List<Product> products = productRepository.findByNameContainsIgnoreCase(name);
            List<ProductResponseDto> responseDto = products.stream()
                    .map(this::mappedToProductResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, responseDto, "Successful"));

    }

    @Override
    public ResponseEntity<CustomResponse> getProductByCategory(String categoryName) {
        List<Product> products = productRepository.findByCategoryNameContainsIgnoreCase(categoryName);
        List<ProductResponseDto> responseDtoList = products
                .stream()
                .map((this::mappedToProductResponse))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, responseDtoList, "Successfully"));
    }

    @Override
    public ResponseEntity<CustomResponse> getProductByPriceRange(Double min, Double max) {
        List<Product> products = productRepository.findByPriceBetween(min, max);
        List<ProductResponseDto> responseDtoList = products.stream()
                .map((this::mappedToProductResponse))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, responseDtoList, "Successfully"));
    }

    @Override
    public ResponseEntity<CustomResponse> deleteProduct(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if(!productOpt.isPresent()){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "No Product found"));
        }

        Product product = productOpt.get();

        List<ProductCategory> categories = categoryRepository.findByProducts(product);

        for(ProductCategory category: categories){
            categoryService.removeAllProductFromCategory(category.getId());
        }

        productImageService.deleteImagesByProductId(product);

        productRepository.delete(product);
        return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, "Successful"));
    }

    @Override
    public ResponseEntity<CustomResponse> UpdateProduct(Long productId, ProductRequestDto requestDto) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if(!productOpt.isPresent()){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "No Product found"));
        }

        Product product = productOpt.get();
        List<ProductCategory> categories = categoryRepository.findByProducts(product);
        product.setName(requestDto.getName());
        product.setDescription(requestDto.getDescription());
        product.setPrice(requestDto.getPrice());
        product.setUnit(requestDto.getUnit());
        product.setCategory(categories);

        productRepository.save(product);
        return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, product,"Successful"));
    }

    private ProductResponseDto mappedToProductResponse(Product product){
        List<String> imagePaths = getImagePaths(product);
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .categories(product.getCategory().stream().toList())
                .price(product.getPrice())
                .unit(product.getUnit())
                .productImagePath(imagePaths)
                .build();
    }
    private List<String> getImagePaths(Product product){
        List<String> imagePaths = imageRepository.findByImagePath(product);

        return imagePaths;
    }
}
