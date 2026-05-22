package com.ms.orderservice.messaging.consumer.order_product;


import com.example.sharedfilesmodule.dtos.StockValidationResponseDto;
import com.ms.orderservice.services.OrderServiceInternal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMessagingListener {
    private final OrderServiceInternal orderServiceInternal;


    @KafkaListener(topics = "order.stock-validation-result", groupId = "order-service")
    public void handleStock(StockValidationResponseDto stockValidationResponseDto) {
        orderServiceInternal.processStockValidation(stockValidationResponseDto);
    }
}
