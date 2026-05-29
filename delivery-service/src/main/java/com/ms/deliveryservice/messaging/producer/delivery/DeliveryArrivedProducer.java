package com.ms.deliveryservice.messaging.producer.delivery;

import com.example.sharedfilesmodule.dtos.notification.OrderNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeliveryArrivedProducer {
    private static final String ORDER_ARRIVED_TOPIC = "order-arrived-notification";
    private final KafkaEventDispacher kafkaEventDispacher;
    public void sendMessageToUserWhenOrderArrived(UUID userId){
        OrderNotificationDto orderNotificationDto = new OrderNotificationDto(userId, "You order has arrived");
        kafkaEventDispacher.sendMessage(ORDER_ARRIVED_TOPIC, userId.toString(), orderNotificationDto);
    }
}
