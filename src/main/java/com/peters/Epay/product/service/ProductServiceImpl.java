package com.peters.Epay.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peters.Epay.async.AsyncRunner;
import com.peters.Epay.exception.DuplicateException;
import com.peters.Epay.exception.ProductNotFoundException;
import com.peters.Epay.product.dto.*;
import com.peters.Epay.product.entity.Product;
import com.peters.Epay.product.entity.ProductCategory;
import com.peters.Epay.product.entity.ProductOrder;
import com.peters.Epay.product.repository.IProductOrderRepository;
import com.peters.Epay.product.repository.IProductRepository;
import com.peters.Epay.product.repository.ProductCategoryRepository;
import com.peters.Epay.product.repository.ProductImageRepository;
import com.peters.Epay.user.dto.CustomResponse;
import com.peters.Epay.user.entity.UserAddress;
import com.peters.Epay.user.entity.UserEntity;
import com.peters.Epay.user.repository.IUserAddressRepository;
import com.peters.Epay.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final IUserAddressRepository addressRepository;
    private final IProductOrderRepository orderRepository;
    private final HashOperations<String, String, Object> hashOperations;
    private final AsyncRunner asyncRunner;

    private final RedisTemplate redisTemplate;

    private static final String PRODUCT_CACHE = "products";
    private static final String CART_ITEM = "cart";
    private static final String ORDER_CACHE = "order";
    @Override
    public ResponseEntity<CustomResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> responseDtoList=  products.stream().map((this::mappedToProductResponse)).collect(Collectors.toList());
        return ResponseEntity.ok(new CustomResponse(HttpStatus.OK.name(), Arrays.asList(responseDtoList), "Successful"));
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
            CustomResponse customResponse = new CustomResponse(HttpStatus.OK.name(), responseDto, "Successful");
            hashOperations.put(PRODUCT_CACHE, name, customResponse);
            return ResponseEntity.ok(customResponse);

    }

    @Override
    public ResponseEntity<CustomResponse> getProductById(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if(!productOpt.isPresent()){
            throw new ProductNotFoundException("No product found");
        }
        CustomResponse cacheResult = (CustomResponse) hashOperations.get(PRODUCT_CACHE, productId.toString());
        if(cacheResult!=null){
            log.info("fetched result from cache {}: {}",PRODUCT_CACHE, cacheResult);
            return ResponseEntity.ok(cacheResult);
        }
        List<String> imagePaths = getImagePaths(productOpt.get());
        ProductResponseDto responseDto = ProductResponseDto.builder()
                .id(productOpt.get().getId())
                .name(productOpt.get().getName())
                .price(productOpt.get().getPrice())
                .unit(productOpt.get().getUnit())
                .description(productOpt.get().getDescription())
                .categories(productOpt.get().getCategory().stream().toList())
                .productImagePath(imagePaths)
                .build();
        CustomResponse customResponse = new CustomResponse(HttpStatus.OK.name(), responseDto, "Successful");
        hashOperations.put(PRODUCT_CACHE, productId.toString(), customResponse);
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
        CustomResponse customResponse = new CustomResponse(HttpStatus.OK.name(), responseDtoList, "Successful");
        hashOperations.put(PRODUCT_CACHE, categoryName, customResponse);
        return ResponseEntity.ok(customResponse);
    }

    @Override
    public ResponseEntity<CustomResponse> getProductByPriceRange(Double min, Double max) {
        String cacheKey = generateCacheKey(min, max);
        Object cachedResponseObj = hashOperations.get(PRODUCT_CACHE, cacheKey);
        if (cachedResponseObj != null && cachedResponseObj instanceof CustomResponse) {
            CustomResponse cachedResponse = (CustomResponse) cachedResponseObj;
            log.info("Fetched product result from cache {} ", PRODUCT_CACHE);
            return ResponseEntity.ok(cachedResponse);
        }

        List<Product> products = productRepository.findByPriceBetween(min, max);
        List<ProductResponseDto> responseDtoList = products.stream()
                .map((this::mappedToProductResponse))
                .collect(Collectors.toList());
        CustomResponse customResponse = new CustomResponse(HttpStatus.OK.name(), responseDtoList, "Successfully");
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
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST.name(), "error","No product found!"));
        }

        // Perform the partial update
        BeanUtils.copyProperties(requestDto, existingProduct, getNullPropertyNames(requestDto));

        // Save the updated product
        Product updatedProduct = productRepository.save(existingProduct);
        List<String> imagePaths = getImagePaths(updatedProduct);
        ProductResponseDto response = ProductResponseDto.builder()
                .id(updatedProduct.getId())
                .name(updatedProduct.getName())
                .description(updatedProduct.getDescription())
                .price(updatedProduct.getPrice())
                .categories(updatedProduct.getCategory().stream().toList())
                .productImagePath(imagePaths)
                .build();

        // Return the response
        if (updatedProduct != null) {
            return ResponseEntity.ok(new CustomResponse(HttpStatus.OK.name(), response,"Successfully updated product"));
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

    @Override
    public ResponseEntity<CartResponse> addProductToCart(Long productId, int unit) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if(!productOpt.isPresent()){
            throw new ProductNotFoundException("No product found");
        }
        int productUnit = productOpt.get().getUnit();
        if(unit>productUnit){
            return ResponseEntity.badRequest().body(new CartResponse(HttpStatus.BAD_REQUEST.toString(), "Out of stock"));
        }
        double subTotal = productOpt.get().getPrice() * unit;
        CartResponse cartResponse = CartResponse.builder()
                .productName(productOpt.get().getName())
                .amount(productOpt.get().getPrice())
                .unit(unit)
                .subtotal(subTotal)
                .message("Successful")
                .status(HttpStatus.OK.toString())
                .build();
        //save to redis
        redisTemplate.opsForHash().put(CART_ITEM, productId.toString(), cartResponse);

        return ResponseEntity.ok(cartResponse);
    }
    @Override
    public ResponseEntity<List<CartResponse>> getAllCarts() {
        List<CartResponse> carts = redisTemplate.opsForHash().values(CART_ITEM);
        return ResponseEntity.ok(carts);
    }

    @Override
    public ResponseEntity<CartResponse> deleteProductFromCart(Long productId) {
        redisTemplate.opsForHash().delete(CART_ITEM, productId.toString());
        return ResponseEntity.ok(new CartResponse(HttpStatus.OK.name().toString(), "Successfully deleted product from cart"));
    }

    @Override
    public ResponseEntity<CartResponse> clearCart() {
        redisTemplate.delete(CART_ITEM);
        return ResponseEntity.ok(new CartResponse(HttpStatus.OK.name().toString(), "Successfully clear cart"));
    }

    @Override
    public ResponseEntity<?> checkout(Long userId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new CartResponse(HttpStatus.BAD_REQUEST.name(), "No user found!"));
        }
        UserEntity user = userOpt.get();

        if (!user.isEnabled()) {
            return ResponseEntity.badRequest().body(new CartResponse(HttpStatus.BAD_REQUEST.name(), "Your account is locked. Kindly proceed to your email to complete your registration"));
        }

        Optional<UserAddress> address = addressRepository.findByUser(user);
        if (!address.isPresent() || address.isEmpty()) {
            return ResponseEntity.badRequest().body(new CartResponse(HttpStatus.BAD_REQUEST.name(), "Kindly add destination address and phone number"));
        }

        List<Object> cartItems = redisTemplate.opsForHash().values(CART_ITEM);
        List<CartResponse> carts = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        for (Object cartItem : cartItems) {
            CartResponse cart = mapper.convertValue(cartItem, CartResponse.class);
            carts.add(cart);
        }

        if (!carts.isEmpty()) {
            double totalAmount = 0.0;

            for (CartResponse cart : carts) {
                totalAmount += cart.getSubtotal();
            }

            String orderReference = UUID.randomUUID().toString();

            ProductOrder order = ProductOrder.builder()
                    .totalAmount(totalAmount)
                    .products(carts)
                    .reference(orderReference)
                    .user(user)
                    .status("Processing")
                    .build();
            OrderResponse response = OrderResponse.builder()
                    .orderReference(order.getReference())
                    .totalAmount(totalAmount)
                    .products(carts)
                    .build();
            //store to cache
            redisTemplate.opsForHash().put(ORDER_CACHE, user.getId().toString(), response);

            orderRepository.save(order);
            redisTemplate.delete(CART_ITEM);
            OrderMessageNotification notification = OrderMessageNotification.builder()
                    .userFullName(user.getFirstName()+" "+user.getLastName())
                    .userEmail(user.getEmail())
                    .orderReference(order.getReference())
                    .totalAmount(order.getTotalAmount())
                    .products(carts)
                    .build();

            asyncRunner.sendOrderNotification(notification);
            return ResponseEntity.ok(new CustomResponse(HttpStatus.OK.name(), response, "Successfully placed order"));
        }
        return ResponseEntity.badRequest().body(new CartResponse(HttpStatus.BAD_REQUEST.name(), "Your cart is empty. Kindly add some products to your cart to continue."));
    }

    @Override
    public ResponseEntity<CustomResponse> getOrders() {


        List<Object> orderItems = redisTemplate.opsForHash().values(ORDER_CACHE);
        List<OrderResponse> orders = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        for (Object cartItem : orderItems) {
            OrderResponse order = mapper.convertValue(cartItem, OrderResponse.class);
            orders.add(order);
        }
        return ResponseEntity.ok(new CustomResponse(HttpStatus.OK.name(), Arrays.asList(orders), "Successfully fetch orders" ));
    }

}
