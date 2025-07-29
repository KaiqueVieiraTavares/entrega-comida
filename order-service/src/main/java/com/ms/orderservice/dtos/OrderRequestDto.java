package com.ms.orderservice.dtos;

import java.util.List;


public record OrderRequestDto(
        List<OrderItemRequestDto> items
) {}

