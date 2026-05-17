package com.example.sharedfilesmodule.dtos.restaurant;

import java.util.UUID;

public record RestaurantCreatedEvent(
        UUID ownerID,
        UUID restaurantId
) {
}
