package com.peters.User_Registration_and_Email_Verification.user.exception;

public class DuplicateException extends RuntimeException{
    private final transient Object[] args;

    public DuplicateException() {
        args = new Object[] {};
    }

    public DuplicateException(String message) {
        super(message);
        args = new Object[] {};
    }

    public DuplicateException(Object[] args) {
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }
}
