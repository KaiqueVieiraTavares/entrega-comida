package com.ms.deliveryservice.messaging.producer.delivery;

import com.example.sharedfilesmodule.dtos.notification.OrderNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Slf4j
@RequiredArgsConstructor
@Component
public class DeliveryCancelledProducer {
    private final KafkaEventDispacher kafkaEventDispacher;
    private static final String ORDER_CANCELED_TOPIC = "order-canceled-notification";
    public void sendMessageToUserWhenOrderIsCanceled(UUID userId){
        OrderNotificationDto orderNotificationDto = new OrderNotificationDto(userId, "Your order has been canceled!");
        kafkaEventDispacher.sendMessage(ORDER_CANCELED_TOPIC, userId.toString(), orderNotificationDto);
    }
}
