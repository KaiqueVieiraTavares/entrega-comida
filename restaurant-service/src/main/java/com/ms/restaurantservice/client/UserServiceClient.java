package com.ms.restaurantservice.client;

import com.example.sharedfilesmodule.dtos.user.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/users/{userId}/exists")
    Boolean existsByUserId(@PathVariable("userId") UUID userId);
}
