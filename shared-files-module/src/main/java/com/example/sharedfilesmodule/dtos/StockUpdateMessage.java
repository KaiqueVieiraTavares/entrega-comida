package com.example.sharedfilesmodule.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record StockUpdateMessage(
        @NotEmpty(message = "items cannot be empty")
        @Valid
        List<StockItemDto> items
) {}