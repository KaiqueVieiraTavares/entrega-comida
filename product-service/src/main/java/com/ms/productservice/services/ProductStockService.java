package com.ms.productservice.services;

import com.example.sharedfilesmodule.dtos.StockValidationRequestDto;
import com.example.sharedfilesmodule.dtos.StockValidationResponseDto;
import com.ms.productservice.exceptions.InsufficientStockException;
import com.ms.productservice.exceptions.ProductNotFoundException;
import com.ms.productservice.message.producer.ProductProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductStockService {
    private final ProductProducer productProducer;
    private final ProductStockTransactionalService productStockTransactionalService;

    public void validateStockRequest(StockValidationRequestDto stockValidationRequestDto){
        try{
           productStockTransactionalService.executeStockValidation(stockValidationRequestDto);
            var response = new StockValidationResponseDto(stockValidationRequestDto.orderId(), "VALID", BigDecimal.ZERO);
            productProducer.sendValidationResult(response);
        } catch (ProductNotFoundException e){
            log.error(
                    "Product not found during stock validation. OrderId: {}",
                    stockValidationRequestDto.orderId()
            );
            var response = new StockValidationResponseDto(stockValidationRequestDto.orderId(), "INVALID", BigDecimal.ZERO);
            productProducer.sendValidationResult(response);
        } catch (InsufficientStockException e){
            var response = new StockValidationResponseDto(stockValidationRequestDto.orderId(), "INVALID", BigDecimal.ZERO);
            productProducer.sendValidationResult(response);
        }
    }

}
