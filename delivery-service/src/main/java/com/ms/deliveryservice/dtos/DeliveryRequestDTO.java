package com.ms.deliveryservice.dtos;

import java.util.UUID;

public record DeliveryRequestDTO(
        UUID orderId,
        UUID restaurantId,
        String deliveryAddress
) {
}
