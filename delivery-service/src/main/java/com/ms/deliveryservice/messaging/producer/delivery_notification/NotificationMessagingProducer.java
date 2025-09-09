package com.ms.deliveryservice.messaging.producer.delivery_notification;

import com.example.sharedfilesmodule.dtos.notification.OrderNotificationDto;
import com.example.sharedfilesmodule.enums.DeliveryStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class NotificationMessagingProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String ORDER_ON_ROUTE_TOPIC = "order-on-route-notification";
    private static final String ORDER_CANCELED_TOPIC = "order-canceled-notification";
    private static final String ORDER_ARRIVED_TOPIC = "order-arrived-notification";
    public NotificationMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessageWhenOrderIsOnRoute(UUID userId){
        sendMessage(ORDER_ON_ROUTE_TOPIC, new OrderNotificationDto(userId, "Your order is on route!"));
    }
    public void sendMessageToUserWhenOrderIsCanceled(UUID userId){
        sendMessage(ORDER_CANCELED_TOPIC, new OrderNotificationDto(userId, "Your order has been canceled!"));
    }
    public void sendMessageToUserWhenOrderArrived(UUID userId){
        sendMessage(ORDER_ARRIVED_TOPIC, new OrderNotificationDto(userId, "Your order has arrived"));
    }

    private void sendMessage(String topic, OrderNotificationDto dto) {
        kafkaTemplate.send(topic, dto).whenComplete((sendResult, ex) -> {
            if(ex != null){
                log.error("failed to send message with id: {}, error: {}", dto.id(), ex.getMessage());
            } else {
                log.info("Message sent successfully to user with id: {}! Topic: {}, Partition: {}, Offset: {}",
                        dto.id(),
                        sendResult.getRecordMetadata().topic(),
                        sendResult.getRecordMetadata().partition(),
                        sendResult.getRecordMetadata().offset());
            }
        });
    }

}
