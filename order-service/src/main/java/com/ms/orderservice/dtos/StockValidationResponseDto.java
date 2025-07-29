package com.ms.orderservice.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record StockValidationResponseDto(
        UUID orderId,
        String status,
        BigDecimal totalPrice,
        String reason
         ) {}
