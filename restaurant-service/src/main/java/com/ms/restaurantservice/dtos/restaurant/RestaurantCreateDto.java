package com.ms.restaurantservice.dtos.restaurant;


import com.ms.restaurantservice.enums.Category;

public record RestaurantCreateDto(
        String name,
        String email,
        String password,
        String cnpj,
        String phone,
        String address,
        String city,
        String state,
        String cep,
        String description,
        Category category
) {
}

