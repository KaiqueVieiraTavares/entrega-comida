package com.ms.productservice.services;

import com.example.sharedfilesmodule.dtos.StockValidationRequestDto;
import com.ms.productservice.exceptions.InsufficientStockException;
import com.ms.productservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductStockTransactionalService {
    private final ProductRepository productRepository;
    @Transactional
    public void executeStockValidation(StockValidationRequestDto stockValidationRequestDto){
        for(var item : stockValidationRequestDto.items()) {
            int updatedRows = productRepository.decreaseStock(item.productId(), item.quantity());
            if(updatedRows == 0){
                throw new InsufficientStockException("Insufficient stock or product not found for product: " + item.productId());
            }
        }
    }
}
