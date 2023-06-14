package com.peters.User_Registration_and_Email_Verification.controller;

import com.peters.User_Registration_and_Email_Verification.product.dto.CartResponse;
import com.peters.User_Registration_and_Email_Verification.product.service.IProductService;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.user.dto.LoginRequestDto;
import com.peters.User_Registration_and_Email_Verification.user.dto.UserAddressRequest;
import com.peters.User_Registration_and_Email_Verification.user.dto.UserRequestDto;
import com.peters.User_Registration_and_Email_Verification.user.service.IUserService;
import com.peters.User_Registration_and_Email_Verification.user.service.UserAuthenticationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final UserAuthenticationService authenticationService;
    private final IProductService productService;

    @PostMapping
    public ResponseEntity<CustomResponse> register(@RequestBody UserRequestDto requestDto){
        return userService.registerUser(requestDto);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<CustomResponse> verifyEmail(@RequestParam("token") String token){
        return userService.verifyEmail(token);
    }

    @GetMapping("/resend-token")
    public ResponseEntity<?> resendVerificationToken(@RequestParam("token") String oldToken) throws MessagingException, UnsupportedEncodingException {
      return userService.resendVerificationTokenEmail(oldToken);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<CustomResponse> authenticateUser(@RequestBody @Validated LoginRequestDto loginRequest) throws Exception {
        return authenticationService.createAuthenticationTokenAndAuthenticateUser(loginRequest);
    }

    @GetMapping("/products")
    public ResponseEntity<CustomResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<CartResponse> addToCart(@RequestParam("productId") Long productId, @RequestParam("unit") int unit){
        return productService.addProductToCart(productId, unit);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartResponse>> getAllCarts(){
        return productService.getAllCarts();
    }

    @DeleteMapping("/remove-from-cart/{productId}")
    public ResponseEntity<CartResponse> deleteProductFromCart(@PathVariable("productId") Long productId){
        return productService.deleteProductFromCart(productId);
    }

    @DeleteMapping("/clear-cart")
    public ResponseEntity<CartResponse> clearCart(){
        return productService.clearCart();
    }

    @PostMapping("/{userId}/add-address")
    public ResponseEntity<CustomResponse> addAddress(@PathVariable Long userId, UserAddressRequest request){
        return userService.addAddress(userId, request);
    }
}
