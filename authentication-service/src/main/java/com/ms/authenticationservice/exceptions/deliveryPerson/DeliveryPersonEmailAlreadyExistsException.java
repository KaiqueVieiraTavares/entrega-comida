package com.ms.authenticationservice.exceptions.deliveryPerson;

public class DeliveryPersonEmailAlreadyExistsException extends RuntimeException {
    public DeliveryPersonEmailAlreadyExistsException( ) {
        super("Email already exists!");
    }
}
