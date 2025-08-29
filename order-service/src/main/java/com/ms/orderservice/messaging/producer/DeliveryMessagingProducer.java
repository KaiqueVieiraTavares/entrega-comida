package com.ms.orderservice.messaging.producer;

import com.example.sharedfilesmodule.dtos.OrderConfirmedDtoDelivery;
import com.ms.orderservice.feignclient.RestaurantServiceClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeliveryMessagingProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RestaurantServiceClient restaurantServiceClient;
    public DeliveryMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate, RestaurantServiceClient restaurantServiceClient) {
        this.kafkaTemplate = kafkaTemplate;
        this.restaurantServiceClient = restaurantServiceClient;
    }

    public void sendOrderConfirmed(UUID orderId, UUID restaurantId){
        String address = restaurantServiceClient.getAddress(restaurantId);

        var confirmedOrder = new OrderConfirmedDtoDelivery(orderId, restaurantId, address);
        kafkaTemplate.send("order-confirmed", confirmedOrder);
    }
}
