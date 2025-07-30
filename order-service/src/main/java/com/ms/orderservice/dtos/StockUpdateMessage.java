package com.ms.orderservice.dtos;

import java.util.List;
import java.util.UUID;

public record StockUpdateMessage
        (List<StockItemDto> items) {}

