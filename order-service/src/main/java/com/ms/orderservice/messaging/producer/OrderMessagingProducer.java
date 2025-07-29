package com.ms.orderservice.messaging.producer;


import com.ms.orderservice.dtos.StockUpdateMessage;
import com.ms.orderservice.dtos.StockValidationRequestDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMessagingProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendStockValidationRequest(StockValidationRequestDto stockValidationRequestDto){
        kafkaTemplate.send("order.validate-stock", stockValidationRequestDto);
    }

    public void sendStockUpdate(UUID productId, int quantityChange){
        var message = new StockUpdateMessage(productId, quantityChange);
        kafkaTemplate.send("order-update-stock", message);
    }
}
