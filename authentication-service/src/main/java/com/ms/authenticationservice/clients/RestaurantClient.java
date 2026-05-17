package com.ms.authenticationservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Service
@FeignClient(name = "restaurant-service")
public interface RestaurantClient {

    @GetMapping("/me/id")
    UUID getMyrestaurantId();
}
