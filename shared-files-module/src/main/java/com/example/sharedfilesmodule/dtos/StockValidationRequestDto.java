package com.example.sharedfilesmodule.dtos;

import java.util.List;
import java.util.UUID;

public record StockValidationRequestDto(
        UUID orderId,
        UUID clientId,
        List<StockItemDto> items
) {}