package com.peters.User_Registration_and_Email_Verification.service;

import com.peters.User_Registration_and_Email_Verification.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.dto.UserRequestDto;
import com.peters.User_Registration_and_Email_Verification.dto.UserResponseDto;
import com.peters.User_Registration_and_Email_Verification.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.entity.VerificationToken;
import com.peters.User_Registration_and_Email_Verification.event.RegistrationCompletePublisher;
import com.peters.User_Registration_and_Email_Verification.repository.IUserRepository;
import com.peters.User_Registration_and_Email_Verification.repository.IVerificationTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IVerificationTokenRepository tokenRepository;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    private final ApplicationEventPublisher publisher;
    @Override
    public ResponseEntity<CustomResponse> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        List<UserResponseDto> userResponseList = users.stream()
                .map(user -> mapToUserResponse(user)).collect(Collectors.toList());

        CustomResponse successResponse = CustomResponse.builder()
                .status(HttpStatus.OK)
                .message("Successful")
                .data(userResponseList.isEmpty() ? null : userResponseList)
                .build();

        return ResponseEntity.ok(successResponse);
    }

    private UserResponseDto mapToUserResponse(UserEntity user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .role(user.getRole())
                .isEnabled(user.isEnabled())
                .build();
    }

    @Override
    public ResponseEntity<CustomResponse> registerUser(UserRequestDto request, final HttpServletRequest servletRequest) {
        Optional<UserEntity> userOpt = userRepository.findUserByEmail(request.getEmail());
        if(userOpt.isPresent()){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "User already exists"));
        }
        if(request == null){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "Request body is required"));
        }
        if(request.getEmail() == null){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "email is required"));
        }
        if(!validateEmail(request.getEmail())){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "provide correct email format"));
        }
        if(request.getPassword() == null){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "password is required"));
        }
        if(request.getFirstName() == null){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "firstName is required"));
        }
        if(request.getLastName() == null){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "lastName is required"));
        }
        if(request.getRole() == null){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "role is required"));
        }
        UserEntity newUser = UserEntity.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(newUser);

        // Publish
        publisher.publishEvent(new RegistrationCompletePublisher(newUser, applicationUrl(servletRequest)));
        return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, "Successful! Kindly check your mail to verify your email address."));
    }

    private String applicationUrl(HttpServletRequest servletRequest) {
        return "http://"+servletRequest.getServerName()+":"+servletRequest.getServerPort()+servletRequest.getContextPath();
    }

    @Override
    public ResponseEntity<CustomResponse> findUserByEmail(String email) {
        Optional<UserEntity> userOpt = userRepository.findUserByEmail(email);
        if(!userOpt.isEmpty()){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "No user with email address found"));
        }
        UserEntity user = userOpt.get();
        UserResponseDto response = UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .isEnabled(user.isEnabled())
                .build();
        return ResponseEntity.ok(new CustomResponse(HttpStatus.OK, Arrays.asList(response), "Successful"));
    }

    @Override
    public void saveVerificationToken(UserEntity theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }

    @Override
    public ResponseEntity<CustomResponse> verifyEmail(String token) {
        Optional<VerificationToken> tokenOpt = tokenRepository.findByToken(token);
        if(!tokenOpt.isPresent()){
            return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "No token found"));
        }
        VerificationToken theToken = tokenOpt.get();
        if(theToken.getUser().isEnabled()){
            return ResponseEntity.ok().body(new CustomResponse(HttpStatus.FOUND, "This user has already been verified, please login"));
        }
        CustomResponse verficationResult = validateToken(token);
        if(verficationResult.getMessage().equalsIgnoreCase("Valid")){
            return ResponseEntity.ok().body(new CustomResponse(HttpStatus.OK, "Email verified successfully. Kindly proceed to login"));
        }
        return ResponseEntity.badRequest().body(new CustomResponse(HttpStatus.BAD_REQUEST, "Token is either expired or invalid"));
    }

    private CustomResponse validateToken(String token) {
        Optional<VerificationToken> tokeOpt = tokenRepository.findByToken(token);
        if(!tokeOpt.isPresent()){
            return new CustomResponse(HttpStatus.BAD_REQUEST, "Invalid verification token");
        }
        VerificationToken theToken = tokeOpt.get();

        UserEntity user = theToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((theToken.getExpirationTime().getTime()-calendar.getTime().getTime())<=0){
            tokenRepository.delete(theToken);
            return new CustomResponse(HttpStatus.BAD_REQUEST, "Token has expired");
        }

        user.setEnabled(true);
        userRepository.save(user);

        return new CustomResponse(HttpStatus.OK, "Valid");

    }

    public static boolean validateEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
