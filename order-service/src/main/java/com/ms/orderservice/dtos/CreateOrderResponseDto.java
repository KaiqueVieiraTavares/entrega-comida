package com.ms.orderservice.dtos;

import java.util.UUID;

public record CreateOrderResponseDto(UUID orderId, String message) {}

