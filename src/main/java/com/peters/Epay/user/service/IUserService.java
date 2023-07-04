package com.peters.Epay.user.service;

import com.peters.Epay.user.dto.*;
import com.peters.Epay.user.entity.UserEntity;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface IUserService {

    ResponseEntity<CustomResponse> getAllUsers();
    ResponseEntity<CustomResponse> registerUser(UserRequestDto request);

    ResponseEntity<CustomResponse> findUserByEmail(String email);

    ResponseEntity<CustomResponse> fetchUserById(Long userId);

    void saveVerificationToken(UserEntity theUser, String verificationToken);

    ResponseEntity<CustomResponse> verifyEmail(String token);

    ResponseEntity<?> resendVerificationTokenEmail(String oldToken)throws MessagingException, UnsupportedEncodingException;

    ResponseEntity<CustomResponse> addAddress(Long userId, UserAddressRequest request);

    ResponseEntity<CustomResponse> deleteProfile(Long userId);

    ResponseEntity<CustomResponse> updateProfile(Long userId, UserRequestDto request);

    ResponseEntity<CustomResponse> changePassword(ChangePasswordDTO request);

    ResponseEntity<CustomResponse> uploadProfilePicture(Long userId, MultipartFile file) throws IOException;

    ResponseEntity<CustomResponse> fetchProfilePicture(Long userId);

    ResponseEntity<CustomResponse> resetPassword(String email) throws MessagingException, UnsupportedEncodingException;

    ResponseEntity<CustomResponse> confirmResetPassword(Integer token, ResetPasswordDto request);
}
