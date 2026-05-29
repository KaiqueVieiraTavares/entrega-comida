package com.ms.productservice.message.consumer;

import com.example.sharedfilesmodule.dtos.restaurant.RestaurantCreatedEvent;
import com.example.sharedfilesmodule.dtos.restaurant.RestaurantDeletedEvent;
import com.example.sharedfilesmodule.dtos.restaurant.RestaurantUpdatedEvent;
import com.ms.productservice.services.RestaurantCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RestaurantCreatedConsumer {
    private static final String GROUP_ID="product-service";
    private final RestaurantCacheService restaurantCacheService;
    @KafkaListener(topics = "restaurant.created", groupId = GROUP_ID)
    public void handleRestaurantCreated(RestaurantCreatedEvent restaurantCreatedEvent){
        restaurantCacheService.saveRestaurantMetaData(restaurantCreatedEvent);
    }

    @KafkaListener(topics = "restaurant.updated", groupId = GROUP_ID)
    public void handlerRestaurantUpdated(RestaurantUpdatedEvent restaurantUpdatedEvent){
        restaurantCacheService.updateRestaurantOwner(restaurantUpdatedEvent);
    }
    @KafkaListener(topics = "restaurant.deleted", groupId = GROUP_ID)
    public void handleRestaurantDeleted(RestaurantDeletedEvent restaurantDeletedEvent){
        restaurantCacheService.deleteRestaurantCache(restaurantDeletedEvent);
    }
}
