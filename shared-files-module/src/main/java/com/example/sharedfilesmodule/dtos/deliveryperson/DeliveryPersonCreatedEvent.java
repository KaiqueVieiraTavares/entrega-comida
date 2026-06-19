package com.example.sharedfilesmodule.dtos.deliveryperson;

import com.example.sharedfilesmodule.enums.VehicleType;

import java.util.UUID;

public record DeliveryPersonCreatedEvent(
    UUID authId,
    String username,
    String cnh,
    String vehiclePlate,
    VehicleType vehicleType
) {}
