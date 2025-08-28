package com.example.sharedfilesmodule.dtos;

import java.util.List;

public record StockUpdateMessage
        (List<StockItemDto> items) {}
