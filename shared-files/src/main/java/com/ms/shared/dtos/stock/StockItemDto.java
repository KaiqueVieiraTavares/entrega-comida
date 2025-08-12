package com.ms.shared.dtos.stock;

import java.util.UUID;

public record StockItemDto (UUID productId, int quantity){}
