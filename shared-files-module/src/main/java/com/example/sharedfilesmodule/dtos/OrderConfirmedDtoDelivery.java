package com.example.sharedfilesmodule.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record OrderConfirmedDtoDelivery(

        @NotNull(message = "orderId cannot be null")
        UUID orderId,

        @NotNull(message = "restaurantId cannot be null")
        UUID restaurantId,
        @NotNull(message = "user address cannot be null")
        String userAddress,
        @NotNull(message = "restaurant address cannot be null")
        String restaurantAddress

) {
}