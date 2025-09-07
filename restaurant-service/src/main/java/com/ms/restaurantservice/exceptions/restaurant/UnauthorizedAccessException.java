package com.ms.restaurantservice.exceptions.restaurant;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("You are not authorized to this restaurant");
    }
}
