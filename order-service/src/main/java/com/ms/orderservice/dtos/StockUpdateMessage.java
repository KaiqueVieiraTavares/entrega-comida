package com.ms.orderservice.dtos;

import java.util.UUID;

public record StockUpdateMessage(UUID productId, int quantityChange) {}

