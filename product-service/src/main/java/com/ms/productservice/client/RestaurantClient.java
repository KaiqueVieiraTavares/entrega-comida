package com.ms.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Component
@FeignClient(name = "restaurant-service")
public interface RestaurantClient {
    @GetMapping("/restaurants/{restaurantId}/is-owner")
    ResponseEntity<Boolean> isRestaurantOwner(@PathVariable UUID restaurantId, @RequestParam UUID ownerId);
}
