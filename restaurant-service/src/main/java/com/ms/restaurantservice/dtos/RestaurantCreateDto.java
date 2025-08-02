package com.ms.restaurantservice.dtos;



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
        String category
) {
}

