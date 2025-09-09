package com.example.sharedfilesmodule.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record OrderConfirmedDto(

        @NotNull(message = "order id cannot be null")
        UUID orderId,

        @NotNull(message = "restaurant id cannot be null")
        UUID restaurantId,
        @NotNull(message = "user id cannot be null")
        UUID userId,
        @NotNull(message = "user address cannot be null")
        String userAddress,
        @NotNull(message = "restaurant address cannot be null")
        String restaurantAddress

) {
}