package com.ms.orderservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "restaurant-service")
public interface RestaurantServiceClient {

    @GetMapping("/restaurants/{restaurantId}/address")
    String getAddress(@PathVariable UUID restaurantId);
}
