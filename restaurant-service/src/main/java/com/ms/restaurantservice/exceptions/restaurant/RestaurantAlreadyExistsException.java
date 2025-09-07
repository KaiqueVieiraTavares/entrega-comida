package com.ms.restaurantservice.exceptions.restaurant;

public class RestaurantAlreadyExistsException extends RuntimeException {
    public RestaurantAlreadyExistsException(String message) {
        super(message);

    }
    public RestaurantAlreadyExistsException(){
        super("Restaurant already exists!");
    }
}
