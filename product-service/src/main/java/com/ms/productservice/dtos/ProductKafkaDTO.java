package com.ms.productservice.dtos;

import java.math.BigDecimal;

public record ProductKafkaDTO(
        Long id,
        String name,
        BigDecimal price,
        Boolean available
) {
}
