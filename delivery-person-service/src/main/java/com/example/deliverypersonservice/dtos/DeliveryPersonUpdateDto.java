package com.example.deliverypersonservice.dtos;

import com.example.deliverypersonservice.enums.VehicleType;

public record DeliveryPersonUpdateDto(
        String name,
        String cnh,
        String vehiclePlate,
        VehicleType vehicleType
) {
}
