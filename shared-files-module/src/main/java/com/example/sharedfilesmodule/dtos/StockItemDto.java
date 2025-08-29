package com.example.sharedfilesmodule.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StockItemDto(
        @NotNull(message = "productId cannot be null")
        UUID productId,

        @Min(value = 1, message = "quantity must be at least 1")
        int quantity
) {}