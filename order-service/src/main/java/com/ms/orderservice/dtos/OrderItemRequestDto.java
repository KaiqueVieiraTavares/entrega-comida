package com.ms.orderservice.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemRequestDto(
        UUID productId,
        String name,
        BigDecimal price,
        Integer quantity
) {}

