package com.ms.deliveryservice.messaging.consumer;

import com.example.sharedfilesmodule.dtos.OrderConfirmedDto;
import com.ms.deliveryservice.services.DeliveryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConfirmedConsumer {
    private final DeliveryService deliveryService;

    public OrderConfirmedConsumer(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @KafkaListener(topics = "order-confirmed", groupId = "delivery-group")
    public void handleOrderConfirmedEvent(OrderConfirmedDto orderConfirmedDto){
        deliveryService.createDelivery(orderConfirmedDto);
    }
}
