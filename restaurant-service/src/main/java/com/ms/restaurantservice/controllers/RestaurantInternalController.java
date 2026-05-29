package com.ms.restaurantservice.controllers;

import com.ms.restaurantservice.services.RestaurantInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantInternalController {
    private final RestaurantInternalService restaurantInternalService;

    @GetMapping("/{restaurantId}/is-owner")
    public ResponseEntity<Boolean> isRestaurantOwner(@PathVariable UUID restaurantId, @RequestParam UUID ownerId){
        return ResponseEntity.ok(restaurantInternalService.isRestaurantOwner(restaurantId, ownerId));
    }

    @GetMapping("/{restaurantId}/address")
    public ResponseEntity<String> getAddress(@PathVariable UUID restaurantId){
        return ResponseEntity.ok(restaurantInternalService.getRestaurantAddress(restaurantId));
    }
    @GetMapping("/me/id")
    public ResponseEntity<UUID> getMyRestaurantId(@RequestHeader("X-User-Id") UUID ownerId){
        return ResponseEntity.ok(restaurantInternalService.getRestaurantByOwnerId(ownerId));
    }
}
