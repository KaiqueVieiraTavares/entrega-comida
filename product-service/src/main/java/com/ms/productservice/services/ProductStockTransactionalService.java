package com.ms.productservice.services;

import com.example.sharedfilesmodule.dtos.StockValidationRequestDto;
import com.ms.productservice.exceptions.InsufficientStockException;
import com.ms.productservice.exceptions.ProductNotFoundException;
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
            var product = productRepository.findById(item.productId()).orElseThrow(ProductNotFoundException::new);
            int updatedRows = productRepository.decreaseStock(product.getId(), item.quantity());
            if(updatedRows == 0){
                throw new InsufficientStockException("Insufficient stock for product: " + product.getId());
            }
        }
    }
}
