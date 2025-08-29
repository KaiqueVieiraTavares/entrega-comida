package com.ms.deliveryservice.messaging.listener;

import com.ms.deliveryservice.dtos.DeliveryRequestDTO;
import com.ms.deliveryservice.dtos.OrderConfirmedEvent;
import com.ms.deliveryservice.services.DeliveryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMessageListener {
    private final DeliveryService deliveryService;

    public DeliveryMessageListener(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @KafkaListener(topics = "order-confirmed", groupId = "delivery-group")
    public void handleOrderConfirmedEvent(OrderConfirmedEvent orderConfirmedEvent){
        DeliveryRequestDTO dto = new DeliveryRequestDTO(
                orderConfirmedEvent.orderId(),
                orderConfirmedEvent.restaurantId(),
                orderConfirmedEvent.deliveryAddress()
        );
        deliveryService.createDelivery(dto);
    }
}
