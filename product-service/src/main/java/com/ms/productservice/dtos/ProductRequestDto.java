package com.ms.productservice.dtos;

import com.ms.productservice.enums.Category;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDto(

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Description is required")
        @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
        String description,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Availability must be specified")
        Boolean available,

        @NotBlank(message = "Image URL is required")
        @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL")
        String imageUrl,

        @NotNull(message = "Category is required")
        Category category

) {}
