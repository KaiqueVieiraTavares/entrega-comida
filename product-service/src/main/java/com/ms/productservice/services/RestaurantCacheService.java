package com.ms.productservice.services;

import com.example.sharedfilesmodule.dtos.restaurant.RestaurantCreatedEvent;
import com.example.sharedfilesmodule.dtos.restaurant.RestaurantDeletedEvent;
import com.example.sharedfilesmodule.dtos.restaurant.RestaurantUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantCacheService {
private final StringRedisTemplate stringRedisTemplate;
private static final String KEY_PREFIX="restaurant";
public void saveRestaurantMetaData(RestaurantCreatedEvent restaurantCreatedEvent){
    String key = KEY_PREFIX + restaurantCreatedEvent.restaurantId().toString();
    String value = restaurantCreatedEvent.ownerID().toString();
    buildRestaurantCache(key, value, "Successfully cached restaurant metadata");
}
public void updateRestaurantOwner(RestaurantUpdatedEvent restaurantUpdatedEvent){
    String key = KEY_PREFIX + restaurantUpdatedEvent.restaurantId().toString();
    String value = restaurantUpdatedEvent.ownerID().toString();
    buildRestaurantCache(key, value, "Restaurant owner updated in cache");
}
public void deleteRestaurantCache(RestaurantDeletedEvent restaurantDeletedEvent){
    String key = KEY_PREFIX + restaurantDeletedEvent.restaurantId().toString();
    stringRedisTemplate.delete(key);
}
public Boolean isRestaurantOwner(UUID restaurantId, UUID ownerId){
    String key = KEY_PREFIX + restaurantId.toString();
    String cachedOwner = stringRedisTemplate.opsForValue().get(key);
    if(cachedOwner == null){
        log.warn("Cache miss or redis down for Restaurant: {}", restaurantId);
        return null;
    }
    return cachedOwner.equals(ownerId.toString());
}
private void buildRestaurantCache(String key, String value, String info){
    stringRedisTemplate.opsForValue().set(key, value);
    log.info(info);
}
}
