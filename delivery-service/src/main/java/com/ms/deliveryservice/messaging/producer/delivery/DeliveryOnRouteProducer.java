package com.ms.deliveryservice.messaging.producer.delivery;

import com.example.sharedfilesmodule.dtos.notification.OrderNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryOnRouteProducer {

    private final KafkaEventDispacher kafkaEventDispacher;
    private static final String ORDER_ON_ROUTE_TOPIC = "order-on-route-notification";

    public void sendMessageWhenOrderIsOnRoute(UUID userId){
        OrderNotificationDto orderNotificationDto = new OrderNotificationDto(userId, "You order is on route");
        kafkaEventDispacher.sendMessage(ORDER_ON_ROUTE_TOPIC, userId.toString(), orderNotificationDto);
    }
}
