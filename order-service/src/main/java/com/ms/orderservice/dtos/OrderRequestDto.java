package com.ms.orderservice.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrderRequestDto(
        @NotEmpty(message = "Order must have at least one item")
        @Valid
        List<OrderItemRequestDto> items
) {}
