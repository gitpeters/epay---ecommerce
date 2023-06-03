package com.peters.User_Registration_and_Email_Verification.controller;

import com.peters.User_Registration_and_Email_Verification.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.dto.UserRequestDto;
import com.peters.User_Registration_and_Email_Verification.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/register")
@RequiredArgsConstructor
public class NonAuthController {
    private final IUserService userService;

    @PostMapping
    public ResponseEntity<CustomResponse> register(@RequestBody UserRequestDto requestDto, final HttpServletRequest request){
        return userService.registerUser(requestDto, request);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<CustomResponse> verifyEmail(@RequestParam("token") String token){
        return userService.verifyEmail(token);
    }
}
