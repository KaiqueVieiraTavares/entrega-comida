package com.ms.notificationservice.messaging.order_user.consumer;

import com.example.sharedfilesmodule.dtos.notification.OrderNotificationDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderConfirmedEvent {
    private final SimpMessagingTemplate simpMessagingTemplate;


    public OrderConfirmedEvent(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
    @KafkaListener(topics = "order-confirmed-notification", groupId = "notification-service-group")
    public void sendNotificationToUserWhenOrderConfirmed(Object message){
        if(message instanceof OrderNotificationDto){
            String userId = ((OrderNotificationDto) message).id().toString();
            String payload = ((OrderNotificationDto) message).message();
            String path = "/queue/notifications";
            simpMessagingTemplate.convertAndSendToUser(userId, path, payload);
        }
    }
    public void sendNotificationToUserWhenOrderIsCanceled(){

    }
    public void sendNotificationToUserWhenOrderIsOnRoute(){

    }
    public void sendNotificationToUserWhenOrderArrived(){

    }

}
