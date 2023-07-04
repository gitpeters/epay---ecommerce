package com.peters.Epay.controller;

import com.peters.Epay.product.service.IProductService;
import com.peters.Epay.user.dto.ChangePasswordDTO;
import com.peters.Epay.user.dto.CustomResponse;
import com.peters.Epay.user.dto.UserRequestDto;
import com.peters.Epay.user.service.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/auth/user")
@Tag(name = "Authenticated User")
@RequiredArgsConstructor
public class CustomerController {
    private final IUserService userService;
    private final IProductService productService;

    @GetMapping("/{userId}/profile")
    public ResponseEntity<CustomResponse> userProfile(@PathVariable("userId") Long userId){
        return userService.fetchUserById(userId);
    }

    @PostMapping("/change-password")
    public ResponseEntity<CustomResponse> changePassword(@RequestBody ChangePasswordDTO request){
        return userService.changePassword(request);
    }

    @PatchMapping("/{userId}/profile")
    public ResponseEntity<CustomResponse> updateProfile(@PathVariable("userId") Long userId, UserRequestDto request){
        return userService.updateProfile(userId, request);
    }

    @DeleteMapping("/{userId}/profile")
    public ResponseEntity<CustomResponse> deleteProfile(@PathVariable("userId") Long userId){
        return userService.deleteProfile(userId);
    }

    @PostMapping("/{userId}/upload-profle")
    public ResponseEntity<CustomResponse> uploadProfile(@PathVariable Long userId, @RequestParam("profile-image")MultipartFile profile) throws IOException {
        return userService.uploadProfilePicture(userId, profile);
    }

    @GetMapping("/{userId}/profile-picture")
    public ResponseEntity<CustomResponse> fetchProfile(@PathVariable Long userId){
        return userService.fetchProfilePicture(userId);
    }

}
