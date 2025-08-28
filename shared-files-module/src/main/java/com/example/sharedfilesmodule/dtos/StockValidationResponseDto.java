package com.example.sharedfilesmodule.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record StockValidationResponseDto(
        UUID orderId,
        String status,
        BigDecimal totalPrice
) {}
