package com.peters.User_Registration_and_Email_Verification.exception;

import org.springframework.security.core.AuthenticationException;

public class ApplicationAuthenticationException extends AuthenticationException {

    public ApplicationAuthenticationException(String msg, Throwable e) {
        super(msg, e);
    }
    public ApplicationAuthenticationException(String msg) {
        super(msg);
    }
}
