package com.ms.restaurantservice.exceptions;

public class RestaurantAlreadyExistsException extends RuntimeException {
    public RestaurantAlreadyExistsException(String message) {
        super(message);

    }
    public RestaurantAlreadyExistsException(){
        super("Restaurant already exists!");
    }
}
