package com.ms.restaurantservice.controllers;

import com.ms.restaurantservice.dtos.restaurantstaff.RestaurantStaffRequestDTO;
import com.ms.restaurantservice.dtos.restaurantstaff.RestaurantStaffResponseDTO;
import com.ms.restaurantservice.services.RestaurantStaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/staff")
public class RestaurantStaffController {
    private final RestaurantStaffService restaurantStaffService;

    public RestaurantStaffController(RestaurantStaffService restaurantStaffService) {
        this.restaurantStaffService = restaurantStaffService;
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<RestaurantStaffResponseDTO> getStaffFromRestaurant(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable UUID staffId) {

        var response = restaurantStaffService.getStaffFromRestaurant(userId, staffId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<List<RestaurantStaffResponseDTO>> getAllStaffFromRestaurant(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable UUID restaurantId) {

        var response = restaurantStaffService.getAllStaffFromRestaurant(userId, restaurantId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> exitFromRestaurant(@RequestHeader("X-User-Id") UUID userId) {
        restaurantStaffService.exitFromRestaurant(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> deleteStaffFromRestaurant(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable UUID staffId) {

        restaurantStaffService.removeStaffFromRestaurant(userId, staffId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/restaurants/{restaurantId}")
    public ResponseEntity<RestaurantStaffResponseDTO> addStaffFromRestaurant(
            @RequestHeader("X-User-Id") UUID ownerId,
            @PathVariable UUID restaurantId,
            @RequestBody RestaurantStaffRequestDTO restaurantStaffRequestDTO) {

        var response = restaurantStaffService.addStaff(ownerId, restaurantId, restaurantStaffRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
