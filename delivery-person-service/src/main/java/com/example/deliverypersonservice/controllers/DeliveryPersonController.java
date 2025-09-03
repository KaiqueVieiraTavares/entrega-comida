package com.example.deliverypersonservice.controllers;

import com.example.deliverypersonservice.dtos.DeliveryPersonRequestDTO;
import com.example.deliverypersonservice.dtos.DeliveryPersonResponseDTO;
import com.example.deliverypersonservice.services.DeliveryPersonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deliveryPerson")
public class DeliveryPersonController {
    private final DeliveryPersonService deliveryPersonService;

    public DeliveryPersonController(DeliveryPersonService deliveryPersonService) {
        this.deliveryPersonService = deliveryPersonService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryPersonResponseDTO> getDeliveryPerson(@PathVariable UUID id){
        return ResponseEntity.ok(deliveryPersonService.getDeliveryPerson(id));
    }
    @GetMapping()
    public ResponseEntity<List<DeliveryPersonResponseDTO>> getAllDeliveryPerson(){
        return ResponseEntity.ok(deliveryPersonService.getAllDeliveryPerson());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeliveryPerson(@PathVariable UUID id){
        deliveryPersonService.deleteDeliveryPerson(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<DeliveryPersonResponseDTO> updateDeliveryPerson(@PathVariable UUID id, @RequestBody @Valid DeliveryPersonRequestDTO deliveryPersonRequestDTO){
        return ResponseEntity.ok(deliveryPersonService.updateDeliveryPerson(id, deliveryPersonRequestDTO));
    }
}
