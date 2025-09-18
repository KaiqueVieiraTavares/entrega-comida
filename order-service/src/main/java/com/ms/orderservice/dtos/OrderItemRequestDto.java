package com.ms.orderservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemRequestDto(
        @NotNull(message = "Product ID cannot be null")
        UUID productId,

        @NotBlank(message = "Product name cannot be blank")
        String name,

        @NotNull(message = "Price cannot be null")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Quantity cannot be null")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity
) {}
