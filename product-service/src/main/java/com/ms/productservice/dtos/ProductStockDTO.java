package com.ms.productservice.dtos;

public record ProductStockDTO(
        Long productId,
        int quantity
) {
}
