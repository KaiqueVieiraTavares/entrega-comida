package com.ms.restaurantservice.exceptions.restaurantstaff;

public class StaffIsAlreadyInRestaurantException extends RuntimeException {
    public StaffIsAlreadyInRestaurantException(String message) {
        super(message);
    }
}
