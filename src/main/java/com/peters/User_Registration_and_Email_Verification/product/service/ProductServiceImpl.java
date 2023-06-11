package com.peters.User_Registration_and_Email_Verification.product.service;

import com.peters.User_Registration_and_Email_Verification.product.dto.ProductRequestDto;
import com.peters.User_Registration_and_Email_Verification.product.dto.ProductResponseDto;
import com.peters.User_Registration_and_Email_Verification.product.entity.Product;
import com.peters.User_Registration_and_Email_Verification.product.entity.ProductCategory;
import com.peters.User_Registration_and_Email_Verification.product.exception.ProductNotFoundException;
import com.peters.User_Registration_and_Email_Verification.product.repository.IProductRepository;
import com.peters.User_Registration_and_Email_Verification.product.repository.ProductCategoryRepository;
import com.peters.User_Registration_and_Email_Verification.product.repository.ProductImageRepository;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.user.exception.DuplicateException;
import com.peters.User_Registration_and_Email_Verification.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements IProductService{
    private final IProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ProductImageRepository imageRepository;
    private final IUserRepository userRepository;
    private final ICategoryService categoryService;
    private final ProductImageService productImageService;
    private final HashOperations<String, String, Object> hashOperations;

    private static final String PRODUCT_CACHE = "products";

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
                .users(Collections.singleton(user.get()))
                .unit(request.getUnit())
                .build();

        productRepository.save(product);
        return ResponseEntity.ok(new CustomResponse(HttpStatus.CREATED, "Successful"));
    }


    @Override
    public ResponseEntity<CustomResponse> getProductByName(String name){
            CustomResponse cacheResult = (CustomResponse) hashOperations.get(PRODUCT_CACHE, name);
            if(cacheResult!=null){
                log.info("fetched result from cache {}: {}",PRODUCT_CACHE, cacheResult);
                return ResponseEntity.ok(cacheResult);
            }
            List<Product> products = productRepository.findByNameContainsIgnoreCase(name);
            List<ProductResponseDto> responseDto = products.stream()
                    .map(this::mappedToProductResponse)
                    .collect(Collectors.toList());
            CustomResponse customResponse = new CustomResponse(HttpStatus.OK, responseDto, "Successful");
            hashOperations.put(PRODUCT_CACHE, name, customResponse);
            return ResponseEntity.ok(customResponse);

    }

    @Override
    public ResponseEntity<CustomResponse> getProductByCategory(String categoryName) {
        CustomResponse cacheResult = (CustomResponse) hashOperations.get(PRODUCT_CACHE, categoryName);
        if(cacheResult!=null){
            log.info("fetched result from cache {}: {}",PRODUCT_CACHE, cacheResult);
            return ResponseEntity.ok(cacheResult);
        }
        List<Product> products = productRepository.findByCategoryNameContainsIgnoreCase(categoryName);
        List<ProductResponseDto> responseDtoList = products
                .stream()
                .map((this::mappedToProductResponse))
                .collect(Collectors.toList());
        CustomResponse customResponse = new CustomResponse(HttpStatus.OK, responseDtoList, "Successful");
        hashOperations.put(PRODUCT_CACHE, categoryName, customResponse);
        return ResponseEntity.ok(customResponse);
    }

    @Override
    public ResponseEntity<CustomResponse> getProductByPriceRange(Double min, Double max) {
        String cacheKey = generateCacheKey(min, max);
        CustomResponse cachedResponse = (CustomResponse) hashOperations.get(PRODUCT_CACHE, cacheKey);
        if(cachedResponse != null){
            System.out.println("Fetched product result from cache"+ PRODUCT_CACHE);
            return ResponseEntity.ok(cachedResponse);
        }

        List<Product> products = productRepository.findByPriceBetween(min, max);
        List<ProductResponseDto> responseDtoList = products.stream()
                .map((this::mappedToProductResponse))
                .collect(Collectors.toList());
        CustomResponse customResponse = new CustomResponse(HttpStatus.OK, responseDtoList, "Successfully");
        hashOperations.put(PRODUCT_CACHE, cacheKey, customResponse);
        return ResponseEntity.ok(customResponse);
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
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (existingProduct == null) {
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "No product found!"));
        }

        // Perform the partial update
        BeanUtils.copyProperties(requestDto, existingProduct, getNullPropertyNames(requestDto));

        // Save the updated product
        Product updatedProduct = productRepository.save(existingProduct);

        // Return the response
        if (updatedProduct != null) {
            return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, updatedProduct,"Successfully updated product"));
        }
        return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "Something went wrong! Could not update product"));
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

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private String generateCacheKey(double minPrice, double maxPrice) {
        return minPrice + "-" + maxPrice;
    }
}