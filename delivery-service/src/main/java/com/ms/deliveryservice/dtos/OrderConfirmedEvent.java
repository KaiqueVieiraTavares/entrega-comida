package com.ms.deliveryservice.dtos;

import java.util.UUID;

public record OrderConfirmedEvent(
         UUID orderId,
         UUID restaurantId,
         String deliveryAddress
) {
}
