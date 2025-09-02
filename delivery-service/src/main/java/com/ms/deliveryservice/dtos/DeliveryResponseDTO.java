package com.ms.deliveryservice.dtos;





import com.example.sharedfilesmodule.enums.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryResponseDTO(
        UUID id,
        UUID orderId,
        UUID restaurantId,
        UUID deliveryPersonId,
        String deliveryAddress,
        DeliveryStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
