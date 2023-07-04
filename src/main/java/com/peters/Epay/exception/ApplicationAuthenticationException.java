package com.peters.Epay.exception;

import org.springframework.security.core.AuthenticationException;

public class ApplicationAuthenticationException extends AuthenticationException {

    public ApplicationAuthenticationException(String msg, Throwable e) {
        super(msg, e);
    }
    public ApplicationAuthenticationException(String msg) {
        super(msg);
    }
}
