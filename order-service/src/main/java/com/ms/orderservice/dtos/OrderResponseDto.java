package com.ms.orderservice.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponseDto(
        UUID id,
        OrderStatus status,
        BigDecimal totalPrice,
        List<OrderItemResponseDto> items
) {}

