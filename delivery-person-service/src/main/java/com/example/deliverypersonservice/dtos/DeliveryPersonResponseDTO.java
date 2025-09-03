package com.example.deliverypersonservice.dtos;

import com.example.deliverypersonservice.enums.VehicleType;

import java.util.UUID;

public record DeliveryPersonResponseDTO(
        UUID id,
        String name,
        String email,
        String cnh,
        String vehiclePlate,
        VehicleType vehicleType,
        Boolean available,
        Double rating,
        Integer totalRatings
) {}