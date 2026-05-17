package com.ms.restaurantservice.dtos.restaurant;

import java.util.UUID;

public record RestaurantOwnerChangedDto(
        UUID restaurantId,
        UUID newOwnerId){}
