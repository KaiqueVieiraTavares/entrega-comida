package com.ms.restaurantservice.dtos;

import java.util.UUID;

public record RestaurantResponseDto(
        UUID id,
        String name,
        String email,
        String cnpj,
        String phone,
        String address,
        String city,
        String state,
        String cep,
        String description,
        String category
) {
}

