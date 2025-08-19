package com.ms.restaurantservice.controllers;


import com.ms.restaurantservice.dtos.RestaurantCreateDto;
import com.ms.restaurantservice.dtos.RestaurantResponseDto;
import com.ms.restaurantservice.dtos.RestaurantUpdateDto;
import com.ms.restaurantservice.services.RestaurantService;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping()
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@RequestHeader("X-User-Id") String userId, @RequestBody
                                                                  RestaurantCreateDto restaurantCreateDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.
                createRestaurant(UUID.fromString(userId), restaurantCreateDto));
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponseDto> getRestaurantById(@PathVariable UUID restaurantId){
        return ResponseEntity.ok(restaurantService.getRestaurantById(restaurantId));
    }

    @GetMapping()
    public ResponseEntity<List<RestaurantResponseDto>> getAllRestaurants(){
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(@RequestHeader("X-User-Id") String ownerId,
                                                                  @PathVariable UUID restaurantId,@RequestBody RestaurantUpdateDto restaurantUpdateDto){
        return ResponseEntity.ok(restaurantService.updateRestaurant(UUID.fromString(ownerId), restaurantId, restaurantUpdateDto));
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@RequestHeader("X-User-Id") String ownerId,@PathVariable UUID restaurantId){
        restaurantService.deleteRestaurant(UUID.fromString(ownerId), restaurantId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
