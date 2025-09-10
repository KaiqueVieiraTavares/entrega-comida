package com.ms.notificationservice.messaging.order_user.consumer;

import com.example.sharedfilesmodule.dtos.notification.OrderNotificationDto;
import org.hibernate.query.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderNotificationUser {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public static final String PATH="/queue/notifications";
    public OrderNotificationUser(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @KafkaListener(topics = "order-confirmed-notification", groupId = "notification-service-group")
    public void sendNotificationToUserWhenOrderConfirmed(OrderNotificationDto message) {

            String userId =  message.id().toString();
            String payload = message.message();
            sendNotificationToUser(userId,payload);

    }

    @KafkaListener(topics = "order-canceled-notification" )
    public void sendNotificationToUserWhenOrderIsCanceled(OrderNotificationDto message) {
        String userId =  message.id().toString();
        String payload = message.message();
        sendNotificationToUser(userId,payload);
    }

    @KafkaListener(topics = "order-on-route-notification")
    public void sendNotificationToUserWhenOrderIsOnRoute(OrderNotificationDto message) {
        String userId =  message.id().toString();
        String payload = message.message();
        sendNotificationToUser(userId,payload);
    }

    @KafkaListener(topics = "order-arrived-notification")
    public void sendNotificationToUserWhenOrderArrived(OrderNotificationDto message) {
        String userId =  message.id().toString();
        String payload = message.message();
        sendNotificationToUser(userId,payload);
    }

    private void sendNotificationToUser(String userID, String payload){
        simpMessagingTemplate.convertAndSendToUser(userID,PATH,payload);
    }

}
