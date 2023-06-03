package com.peters.User_Registration_and_Email_Verification.controller;

import com.peters.User_Registration_and_Email_Verification.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<CustomResponse> getUser(){
        return userService.getAllUsers();
    }

}
