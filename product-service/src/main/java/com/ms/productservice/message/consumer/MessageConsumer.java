package com.ms.productservice.message.consumer;


import com.example.sharedfilesmodule.dtos.StockUpdateMessage;
import com.example.sharedfilesmodule.dtos.StockValidationRequestDto;
import com.ms.productservice.repositories.ProductRepository;
import com.ms.productservice.services.ProductStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {
    private final ProductRepository productRepository;
    private final ProductStockService productStockService;
    @KafkaListener(topics = "order.validate-stock", groupId = "product-service")
    public void validateStock(StockValidationRequestDto requestDto) {
        productStockService.validateStockRequest(requestDto);
    }
    @KafkaListener(topics = "order.restore-stock", groupId = "product-service")
    public void restoreStock(StockUpdateMessage stockUpdateMessage){
       productStockService.restoreStock(stockUpdateMessage);
    }


}
