package com.ms.restaurantservice.exceptions;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException() {
        super("Restaurant not found");
    }
}
