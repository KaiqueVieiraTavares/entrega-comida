package com.ms.clientservice.exceptions;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
