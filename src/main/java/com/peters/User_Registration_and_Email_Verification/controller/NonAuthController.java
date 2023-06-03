package com.peters.User_Registration_and_Email_Verification.controller;

import com.peters.User_Registration_and_Email_Verification.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.dto.UserRequestDto;
import com.peters.User_Registration_and_Email_Verification.service.IUserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("api/v1/register")
@RequiredArgsConstructor
public class NonAuthController {
    private final IUserService userService;

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


}
