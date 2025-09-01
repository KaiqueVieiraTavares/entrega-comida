package com.ms.orderservice.messaging.producer;

import com.example.sharedfilesmodule.dtos.OrderConfirmedDtoDelivery;
import com.ms.orderservice.exceptions.KafkaSendException;
import com.ms.orderservice.feignclient.RestaurantServiceClient;
import com.ms.orderservice.feignclient.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class DeliveryMessagingProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RestaurantServiceClient restaurantServiceClient;
    private final UserServiceClient userServiceClient;
    public DeliveryMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate, RestaurantServiceClient restaurantServiceClient, UserServiceClient userServiceClient) {
        this.kafkaTemplate = kafkaTemplate;
        this.restaurantServiceClient = restaurantServiceClient;
        this.userServiceClient = userServiceClient;
    }

    public void sendOrderConfirmed(UUID userId, UUID orderId, UUID restaurantId){
        String userAddress = userServiceClient.getAddress(userId);
        String restaurantAddress = restaurantServiceClient.getAddress(restaurantId);
        var confirmedOrder = new OrderConfirmedDtoDelivery(orderId, restaurantId, userAddress, restaurantAddress);
        var future = kafkaTemplate.send("order-confirmed", confirmedOrder);

        future.whenComplete((sendResult,ex) ->{
                if(ex!=null){
                    log.error("error sending order confirmation message", ex);
                } else{

                log.info("order confirmed message sent successfully! Topic: {}, Partition: {}, Offset: {} ",
                        sendResult.getRecordMetadata().topic(),
                        sendResult.getRecordMetadata().partition(),
                        sendResult.getRecordMetadata().offset());

    }
        });
    }


}
