package com.ms.authenticationservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantCacheService {
    private final RedisTemplate<String, String> redisTemplate;
    public String findRestaurantByUserId(UUID userId){
        return redisTemplate.opsForValue().get("restaurant:user" + userId);
    }
}
