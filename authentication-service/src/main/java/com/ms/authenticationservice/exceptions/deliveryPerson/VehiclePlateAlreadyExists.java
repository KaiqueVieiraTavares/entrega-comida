package com.ms.authenticationservice.exceptions.deliveryPerson;

public class VehiclePlateAlreadyExists extends RuntimeException {
    public VehiclePlateAlreadyExists() {
        super("Vehicle plate already exists");
    }
}
