package com.peters.User_Registration_and_Email_Verification.service;

import com.peters.User_Registration_and_Email_Verification.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.dto.UserRequestDto;
import com.peters.User_Registration_and_Email_Verification.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface IUserService {

    ResponseEntity<CustomResponse> getAllUsers();
    ResponseEntity<CustomResponse> registerUser(UserRequestDto request, final HttpServletRequest servletRequest);

    ResponseEntity<CustomResponse> findUserByEmail(String email);

    void saveVerificationToken(UserEntity theUser, String verificationToken);

    ResponseEntity<CustomResponse> verifyEmail(String token);
}
