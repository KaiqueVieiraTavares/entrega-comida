package com.ms.orderservice.messaging.producer.order_notification;

import com.example.sharedfilesmodule.dtos.notification.OrderNotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class NotificationMessagingProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public NotificationMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendOrderConfirmedEvent(UUID userId){
        var orderConfirmedDto = new OrderNotificationDto(userId, "Your order has been confirmed!");
     var future = kafkaTemplate.send("order-confirmed-notification",orderConfirmedDto);

     future.whenComplete((sendResult, ex) -> {
         if(ex!=null){
             log.error("Error sending order confirmed event: {}", ex.getMessage());
         }else{
             log.info("order confirmed message sent successfully! topic: {}, partition: {}, offset: {} ",
                     sendResult.getRecordMetadata().topic(),
                     sendResult.getRecordMetadata().partition(),
                     sendResult.getRecordMetadata().offset());
         }
     });
    }
}
