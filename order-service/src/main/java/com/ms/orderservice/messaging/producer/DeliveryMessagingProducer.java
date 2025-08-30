package com.ms.orderservice.messaging.producer;

import com.example.sharedfilesmodule.dtos.OrderConfirmedDtoDelivery;
import com.ms.orderservice.feignclient.RestaurantServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class DeliveryMessagingProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RestaurantServiceClient restaurantServiceClient;

    public DeliveryMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate, RestaurantServiceClient restaurantServiceClient) {
        this.kafkaTemplate = kafkaTemplate;
        this.restaurantServiceClient = restaurantServiceClient;
    }

    public void sendOrderConfirmed(UUID orderId, UUID restaurantId) {
        String address = restaurantServiceClient.getAddress(restaurantId);

        var confirmedOrder = new OrderConfirmedDtoDelivery(orderId, restaurantId, address);

        kafkaTemplate.send("order-confirmed", confirmedOrder)
                .completable()
                .thenAccept(result -> log.info("Order sent to delivery-service successfully"))
                .exceptionally(ex -> {
                    log.error("Error sending order for delivery-service, error: {}", ex.getMessage());
                    return null;
                });
    }
}
