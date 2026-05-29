package com.ms.restaurantservice.messaging;

import com.example.sharedfilesmodule.dtos.restaurant.RestaurantCreatedEvent;
import com.example.sharedfilesmodule.dtos.restaurant.RestaurantDeletedEvent;
import com.example.sharedfilesmodule.dtos.restaurant.RestaurantUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String RESTAURANT_CREATED_TOPIC = "restaurant-created";
    private static final String RESTAURANT_UPDATED_TOPIC = "restaurant-updated";
    private static final String RESTAURANT_DELETED_TOPIC = "restaurant-deleted";
    public void publishRestaurantCreated(RestaurantCreatedEvent restaurantCreatedEvent){
        var future = kafkaTemplate.send(RESTAURANT_CREATED_TOPIC, restaurantCreatedEvent);
        handleCallBack(future, "restaurant created");
    }
    public void publishRestaurantOwnerShipUpdated(RestaurantUpdatedEvent restaurantUpdatedEvent){
        var future = kafkaTemplate.send(RESTAURANT_UPDATED_TOPIC, restaurantUpdatedEvent);
        handleCallBack(future, "restaurant ownership updated");
    }
    public void publishRestaurantDeleted(RestaurantDeletedEvent restaurantDeletedEvent){
        var future = kafkaTemplate.send(RESTAURANT_DELETED_TOPIC, restaurantDeletedEvent);
        handleCallBack(future, "Restaurant deleted");
    }
    private <K, V> void handleCallBack(CompletableFuture<SendResult<K, V>> future, String contextMessage){
        future.whenComplete((sendResult, ex) -> {
            if(ex!=null){
                log.error("Error sending {} message", contextMessage, ex);
            } else{
                var metaData = sendResult.getRecordMetadata();
                log.info("{} message sent successfully! topic: {} partition: {} offset: {}",
                        contextMessage, metaData.topic(), metaData.partition(), metaData.offset());
            }
        });
    }
}
