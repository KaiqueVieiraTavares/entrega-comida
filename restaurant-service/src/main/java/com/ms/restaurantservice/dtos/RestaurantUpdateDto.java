package com.ms.restaurantservice.dtos;

import com.ms.restaurantservice.enums.Category;

public record RestaurantUpdateDto(
        String name,
        String phone,
        String address,
        String city,
        String state,
        String cep,
        String description,
        Category category
) {
}

