package com.ms.deliveryservice.controllers;


import com.ms.deliveryservice.dtos.DeliveryResponseDTO;
import com.ms.deliveryservice.dtos.UpdateDeliveryStatusDTO;
import com.ms.deliveryservice.services.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("")
    public ResponseEntity<List<DeliveryResponseDTO>> getAvailableDeliveries(){
        var response = deliveryService.getAvailableDeliveries();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<DeliveryResponseDTO>> getMeDeliveries(@RequestHeader("X-User-Id") UUID deliveryPersonId){
     var response = deliveryService.getMeDeliveries(deliveryPersonId);
     return ResponseEntity.ok(response);
    }

    @PostMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponseDTO> assignDelivery(@RequestHeader("X-User-Id") UUID deliveryPersonId,
                                                              @PathVariable UUID deliveryId){
        var response = deliveryService.assignDelivery(deliveryPersonId, deliveryId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<Void> cancelDelivery(@RequestHeader("X-User-Id") UUID deliveryPersonId, @PathVariable UUID deliveryId){
        deliveryService.cancelDelivery(deliveryPersonId, deliveryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PatchMapping("/{deliveryId}/arrived")
    public ResponseEntity<DeliveryResponseDTO> orderArrived(@RequestHeader("X-User-Id") UUID deliveryPersonId, @PathVariable UUID deliveryId){
        var response = deliveryService.orderArrived(deliveryPersonId, deliveryId);
        return ResponseEntity.ok(response);
    }
}
