package com.ms.orderservice.exceptions;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("You do not have access to this content");
    }
}
