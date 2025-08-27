package com.ms.deliveryservice.exceptions;

public class DeliveryAlreadyCompletedException extends RuntimeException {
    public DeliveryAlreadyCompletedException(String message) {
        super(message);
    }
}
