package com.ms.restaurantservice.exceptions.restaurantstaff;

public class OwnerCannotExitException extends RuntimeException {
    public OwnerCannotExitException(String message) {
        super(message);
    }
}
