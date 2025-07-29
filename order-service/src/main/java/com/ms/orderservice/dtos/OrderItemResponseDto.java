package com.ms.orderservice.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDto(
        UUID productId,
        String name,
        Integer quantity,
        BigDecimal price
) {}

