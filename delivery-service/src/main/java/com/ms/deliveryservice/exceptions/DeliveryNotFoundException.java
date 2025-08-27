package com.ms.deliveryservice.exceptions;

public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException(){super("Delivery not found");}
}
