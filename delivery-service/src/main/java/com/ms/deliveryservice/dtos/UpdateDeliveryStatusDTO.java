package com.ms.deliveryservice.dtos;

import com.ms.shared.enums.DeliveryStatus;

public record UpdateDeliveryStatusDTO(
        DeliveryStatus status
) {
}
