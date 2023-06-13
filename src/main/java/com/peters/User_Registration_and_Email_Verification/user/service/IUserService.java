package com.peters.User_Registration_and_Email_Verification.user.service;

import com.peters.User_Registration_and_Email_Verification.user.dto.UserRequestDto;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import jakarta.mail.MessagingException;
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
