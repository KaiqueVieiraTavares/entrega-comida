package com.ms.deliveryservice.dtos;

import com.example.sharedfilesmodule.enums.DeliveryStatus;
public record UpdateDeliveryStatusDTO(
        DeliveryStatus status
) {
}
