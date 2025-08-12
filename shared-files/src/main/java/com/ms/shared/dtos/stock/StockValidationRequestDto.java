package com.ms.shared.dtos.stock;

import java.util.List;
import java.util.UUID;

public record StockValidationRequestDto(
        UUID orderId,
        UUID clientId,
        List<StockItemDto> items
) {}