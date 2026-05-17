package com.ms.restaurantservice.exceptions.restaurantstaff;

public class CannotRemoveRestaurantOwnerException extends RuntimeException {
    public CannotRemoveRestaurantOwnerException(String message) {
        super(message);
    }
}
