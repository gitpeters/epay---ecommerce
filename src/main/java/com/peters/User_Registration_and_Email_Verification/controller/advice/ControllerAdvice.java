package com.peters.User_Registration_and_Email_Verification.controller.advice;

import com.peters.User_Registration_and_Email_Verification.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.exception.ApplicationAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ControllerAdvice {
    @ExceptionHandler(ApplicationAuthenticationException.class)
    public ResponseEntity<CustomResponse> handleApplicationAuthenticationException(ApplicationAuthenticationException e){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        CustomResponse response = new CustomResponse(status, e.getMessage());
        return ResponseEntity.status(status).body(response);
    }
}
