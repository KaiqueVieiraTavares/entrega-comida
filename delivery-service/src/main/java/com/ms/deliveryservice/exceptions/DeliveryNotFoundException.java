package com.ms.deliveryservice.exceptions;

public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException(String message) {
        super(message);
    }
    public DeliveryNotFoundException(){super("Delivery not found");}
}
