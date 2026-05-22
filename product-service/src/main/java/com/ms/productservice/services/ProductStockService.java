package com.ms.productservice.services;

import com.example.sharedfilesmodule.dtos.StockUpdateMessage;
import com.example.sharedfilesmodule.dtos.StockValidationRequestDto;
import com.example.sharedfilesmodule.dtos.StockValidationResponseDto;
import com.example.sharedfilesmodule.enums.StockValidationStatus;
import com.ms.productservice.exceptions.InsufficientStockException;
import com.ms.productservice.exceptions.ProductNotFoundException;
import com.ms.productservice.message.producer.ProductProducer;
import com.ms.productservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductStockService {
    private final ProductRepository productRepository;
    private final ProductProducer productProducer;
    private final ProductStockTransactionalService productStockTransactionalService;

    public void validateStockRequest(StockValidationRequestDto stockValidationRequestDto){
        try{
            //chama método interno para que a exception não trave o fluxo de mensagem
           productStockTransactionalService.executeStockValidation(stockValidationRequestDto);
            var response = new StockValidationResponseDto(stockValidationRequestDto.orderId(), StockValidationStatus.VALID, BigDecimal.ZERO);
            productProducer.sendValidationResult(response);
        } catch (ProductNotFoundException e){
            log.error(
                    "Product not found during stock validation. OrderId: {}",
                    stockValidationRequestDto.orderId()
            );
            var response = new StockValidationResponseDto(stockValidationRequestDto.orderId(), StockValidationStatus.INVALID, BigDecimal.ZERO);
            productProducer.sendValidationResult(response);
        } catch (InsufficientStockException e){
            var response = new StockValidationResponseDto(stockValidationRequestDto.orderId(), StockValidationStatus.INVALID, BigDecimal.ZERO);
            productProducer.sendValidationResult(response);
        }
    }
    @Transactional
    public void restoreStock(StockUpdateMessage stockUpdateMessage){
        for(var item: stockUpdateMessage.items()){
            try {
                int updatedRows = productRepository.increaseStock(item.productId(), item.quantity());
                if (updatedRows == 0) {
                    log.error("Failed to restore stock. Product id: {} not found. Quantity lost: {}", item.productId(), item.quantity());
                }
            } catch (Exception e) {
                log.error("Unexpected error restoring stock for product: {}", item.productId());
            }
        }
    }
}
