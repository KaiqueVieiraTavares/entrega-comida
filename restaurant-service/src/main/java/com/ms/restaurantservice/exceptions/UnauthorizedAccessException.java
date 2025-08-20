package com.ms.restaurantservice.exceptions;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("You are not authorized to this restaurant");
    }
}
