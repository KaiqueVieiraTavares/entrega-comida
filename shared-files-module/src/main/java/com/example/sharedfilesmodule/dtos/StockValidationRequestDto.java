package com.example.sharedfilesmodule.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record StockValidationRequestDto(
        @NotNull(message = "orderId cannot be null")
        UUID orderId,

        @NotNull(message = "clientId cannot be null")
        UUID clientId,

        @NotEmpty(message = "items cannot be empty")
        @Valid
        List<StockItemDto> items
) {}