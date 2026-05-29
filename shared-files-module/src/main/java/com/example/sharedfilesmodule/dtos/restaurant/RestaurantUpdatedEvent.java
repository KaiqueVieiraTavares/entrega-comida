package com.example.sharedfilesmodule.dtos.restaurant;

import java.util.UUID;

public record RestaurantUpdatedEvent(
        UUID ownerID,
        UUID restaurantId
) {
}
