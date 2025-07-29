package com.ms.orderservice.dtos;

import java.util.UUID;

public record StockItemDto(
        UUID productId,
        Integer quantity
) {}

