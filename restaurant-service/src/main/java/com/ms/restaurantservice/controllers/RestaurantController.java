package com.ms.restaurantservice.controllers;


import com.ms.restaurantservice.dtos.restaurant.RestaurantCreateDto;
import com.ms.restaurantservice.dtos.restaurant.RestaurantOwnerChangedDto;
import com.ms.restaurantservice.dtos.restaurant.RestaurantResponseDto;
import com.ms.restaurantservice.dtos.restaurant.RestaurantUpdateDto;
import com.ms.restaurantservice.services.RestaurantService;


import jakarta.validation.Valid;
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
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@RequestHeader("X-User-Id") UUID userId, @Valid @RequestBody
                                                                  RestaurantCreateDto restaurantCreateDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.
                createRestaurant(userId, restaurantCreateDto));
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
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(@RequestHeader("X-User-Id") UUID ownerId,
                                                                  @PathVariable UUID restaurantId,@Valid @RequestBody RestaurantUpdateDto restaurantUpdateDto){
        return ResponseEntity.ok(restaurantService.updateRestaurant(ownerId, restaurantId, restaurantUpdateDto));
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@RequestHeader("X-User-Id") UUID ownerId,@PathVariable UUID restaurantId){
        restaurantService.deleteRestaurant(ownerId, restaurantId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/{restaurantId}/address")
    public ResponseEntity<String> getAddress(@PathVariable UUID restaurantId){
        return ResponseEntity.ok(restaurantService.getRestaurantAddress(restaurantId));
    }
    @GetMapping("/me/id")
    public ResponseEntity<UUID> getMyRestaurantId(@RequestHeader("X-User-Id") UUID ownerId){
        return ResponseEntity.ok(restaurantService.getRestaurantByOwnerId(ownerId));
    }
    @PostMapping("/transfer")
    public ResponseEntity<RestaurantResponseDto> transferOwnerShip(@Valid @RequestBody RestaurantOwnerChangedDto restaurantOwnerChangedEvent, @RequestHeader("X-User-Id") UUID ownerId){
        return ResponseEntity.ok(restaurantService.transferOwnerShip(restaurantOwnerChangedEvent,ownerId));
    }
}
