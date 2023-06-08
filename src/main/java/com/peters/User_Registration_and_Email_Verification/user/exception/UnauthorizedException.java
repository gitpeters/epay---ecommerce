package com.peters.User_Registration_and_Email_Verification.user.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
