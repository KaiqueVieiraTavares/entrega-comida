package com.ms.orderservice.dtos;

import com.ms.orderservice.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponseDto(
        UUID id,
        OrderStatus status,
        BigDecimal totalPrice,
        List<OrderItemResponseDto> items
) {}

