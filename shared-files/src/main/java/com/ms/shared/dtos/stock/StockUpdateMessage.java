package com.ms.shared.dtos.stock;

import java.util.List;

public record StockUpdateMessage
        (List<StockItemDto> items) {}
