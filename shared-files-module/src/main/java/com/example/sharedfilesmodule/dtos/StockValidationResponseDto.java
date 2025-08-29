package com.example.sharedfilesmodule.dtos;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.UUID;

public record StockValidationResponseDto(
        @NotNull(message = "orderId cannot be null")
        UUID orderId,

        @NotBlank(message = "status cannot be blank")
        String status,

        @NotNull(message = "totalPrice cannot be null")
        BigDecimal totalPrice
) {}