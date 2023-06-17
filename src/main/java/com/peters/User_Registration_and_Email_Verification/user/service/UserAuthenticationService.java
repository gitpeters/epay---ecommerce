package com.peters.User_Registration_and_Email_Verification.user.service;

import com.peters.User_Registration_and_Email_Verification.user.dto.LoginResponse;
import com.peters.User_Registration_and_Email_Verification.user.entity.UserEntity;
import com.peters.User_Registration_and_Email_Verification.security.CustomUserDetails;
import com.peters.User_Registration_and_Email_Verification.security.CustomerUserDetailsService;
import com.peters.User_Registration_and_Email_Verification.security.JwtTokenUtil;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.user.dto.LoginRequestDto;
import com.peters.User_Registration_and_Email_Verification.exception.ApplicationAuthenticationException;
import com.peters.User_Registration_and_Email_Verification.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomerUserDetailsService userDetailsService;
    private final IUserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<CustomResponse> createAuthenticationTokenAndAuthenticateUser(LoginRequestDto loginRequest) throws Exception {
        authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(loginRequest.getUsername());
        UserEntity user = userRepository.findUserByEmail(loginRequest.getUsername()).get();

        final String token = jwtTokenUtil.generateToken(userDetails);

        LoginResponse response = LoginResponse.builder()
                .id(user.getId())
                .access_token(token)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .isEnabled(user.isEnabled())
                .build();
        return ResponseEntity.ok(new CustomResponse(HttpStatus.OK.name(), response, "Login successfully"));
    }

    // authenticate user

    private void authenticateUser(String username, String password) throws Exception {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        }catch (DisabledException e){
            throw new Exception("USER_DISABLED", e);
        }catch (BadCredentialsException e){
            throw new ApplicationAuthenticationException("Invalid username or password combination", e);
        }

    }
}
