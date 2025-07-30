package com.ms.orderservice.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemRequestDto(
        UUID productId,
        String name,       // Adicionado
        BigDecimal price,  // Adicionado
        Integer quantity
) {}

