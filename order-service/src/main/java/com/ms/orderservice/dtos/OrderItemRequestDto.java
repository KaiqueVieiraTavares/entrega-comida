package com.ms.orderservice.dtos;

import java.util.UUID;

public record OrderItemRequestDto(
        UUID productId,
        Integer quantity
) {}

