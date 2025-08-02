package com.ms.restaurantservice.dtos;

public record RestaurantUpdateDto(
        String name,
        String phone,
        String address,
        String city,
        String state,
        String cep,
        String description,
        String category
) {
}

