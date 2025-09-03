package com.example.deliverypersonservice.exceptions;

public class DeliveryPersonNotFound extends RuntimeException {
    public DeliveryPersonNotFound() {
        super("Delivery person not found");
    }
}
