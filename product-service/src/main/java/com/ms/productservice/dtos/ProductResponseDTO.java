package com.ms.productservice.dtos;

import com.ms.productservice.enums.Category;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Boolean available,
        String imageUrl,
        Category category
) {
}
