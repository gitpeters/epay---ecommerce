package com.peters.User_Registration_and_Email_Verification.controller.advice;

import com.peters.User_Registration_and_Email_Verification.exception.CategoryAlreadyExistsException;
import com.peters.User_Registration_and_Email_Verification.exception.ProductNotFoundException;
import com.peters.User_Registration_and_Email_Verification.user.dto.CustomResponse;
import com.peters.User_Registration_and_Email_Verification.exception.ApplicationAuthenticationException;
import com.peters.User_Registration_and_Email_Verification.exception.DuplicateException;
import com.peters.User_Registration_and_Email_Verification.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ControllerAdvice {
    private final MessageSource messageSource;
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<CustomResponse> handleDuplicateException(DuplicateException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        CustomResponse response = new CustomResponse(HttpStatus.BAD_REQUEST.name(),
                messageSource.getMessage("duplicate.product.message", e.getArgs(),
                        LocaleContextHolder.getLocale()), null);
        return ResponseEntity.status(status).body(response);
    }
    @ExceptionHandler(ApplicationAuthenticationException.class)
    public ResponseEntity<CustomResponse> handleApplicationAuthenticationException(ApplicationAuthenticationException e){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        CustomResponse response = new CustomResponse(status, e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<CustomResponse> handleUserAlreadyException(UserAlreadyExistsException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomResponse response = new CustomResponse(status, e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<CustomResponse> handleProductNotFoundException(ProductNotFoundException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomResponse response = new CustomResponse(status, e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<CustomResponse> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomResponse response = new CustomResponse(status, e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse> handleGlobalException(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        CustomResponse errorResponse = CustomResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(ex.getLocalizedMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
