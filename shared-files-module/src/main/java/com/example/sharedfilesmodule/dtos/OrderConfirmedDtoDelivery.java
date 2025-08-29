package com.example.sharedfilesmodule.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record OrderConfirmedDtoDelivery(
        @NotNull(message = "orderId cannot be null")
        UUID orderId,

        @NotNull(message = "restaurantId cannot be null")
        UUID restaurantId,

        String deliveryAddress
) {
}