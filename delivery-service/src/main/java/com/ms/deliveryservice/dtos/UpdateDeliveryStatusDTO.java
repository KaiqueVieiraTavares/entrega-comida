package com.ms.deliveryservice.dtos;

import com.ms.deliveryservice.enums.DeliveryStatus;

public record UpdateDeliveryStatusDTO(
        DeliveryStatus status
) {
}
