package com.ms.notificationservice.messaging.order_user.consumer;

import com.example.sharedfilesmodule.dtos.OrderConfirmedDto;
import com.example.sharedfilesmodule.dtos.notification.OrderNotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderNotificationUser {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public static final String PATH="/queue/notifications";

    @KafkaListener(topics = "order-confirmed-notification", groupId = "notification-service-group")
    public void sendNotificationToUserWhenOrderConfirmed(OrderConfirmedDto orderConfirmedDto) {

            String userId =  orderConfirmedDto.userId().toString();
            sendNotificationToUser(userId,"Your order is confirmed!");
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
