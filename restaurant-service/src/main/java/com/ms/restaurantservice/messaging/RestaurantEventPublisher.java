package com.ms.restaurantservice.messaging;

import com.example.sharedfilesmodule.dtos.restaurant.RestaurantCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantEventPublisher {
    private final KafkaTemplate<String, RestaurantCreatedEvent> kafkaTemplate;
    public void publishRestaurantCreated(RestaurantCreatedEvent restaurantCreatedEvent){
        var future = kafkaTemplate.send("restaurant-created", restaurantCreatedEvent);
        future.whenComplete((sendResult, ex) -> {
            if(ex!=null){
                log.error("Error sending restaurant created message");
            } else{
                log.info("restaurant created messaged sent successfuly! topic: {} partition: {} offset: {} ",
                        sendResult.getRecordMetadata().topic(), sendResult.getRecordMetadata().partition(), sendResult.getRecordMetadata().offset());
            }
        });
    }
}
