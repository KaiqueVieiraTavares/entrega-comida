package com.example.deliverypersonservice.dtos;

import com.example.sharedfilesmodule.enums.VehicleType;

public record DeliveryPersonUpdateDto(
        String name,
        String cnh,
        String vehiclePlate,
        VehicleType vehicleType
) {
}
