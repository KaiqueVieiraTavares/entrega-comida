package com.ms.clientservice.exceptions;

public class ClientNotAuthenticatedException extends RuntimeException {
    public ClientNotAuthenticatedException(String message) {
        super(message);
    }
}
