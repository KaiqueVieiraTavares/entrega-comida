package com.ms.authenticationservice.exceptions.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){super(message);}
}
