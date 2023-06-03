package com.peters.User_Registration_and_Email_Verification.service;

import com.peters.User_Registration_and_Email_Verification.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.dto.UserRequestDto;
import com.peters.User_Registration_and_Email_Verification.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.entity.VerificationToken;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;

public interface IUserService {

    ResponseEntity<CustomResponse> getAllUsers();
    ResponseEntity<CustomResponse> registerUser(UserRequestDto request);

    ResponseEntity<CustomResponse> findUserByEmail(String email);

    void saveVerificationToken(UserEntity theUser, String verificationToken);

    ResponseEntity<CustomResponse> verifyEmail(String token);

    ResponseEntity<?> resendVerificationTokenEmail(String oldToken)throws MessagingException, UnsupportedEncodingException;
}
