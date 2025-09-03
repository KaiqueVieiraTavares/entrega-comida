package com.example.deliverypersonservice.exceptions;

public class VehiclePlateAlreadyExists extends RuntimeException {
    public VehiclePlateAlreadyExists() {
        super("Vehicle plate already exists");
    }
}
