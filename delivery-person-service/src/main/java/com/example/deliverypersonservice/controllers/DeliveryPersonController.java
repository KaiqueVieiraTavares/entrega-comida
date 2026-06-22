package com.example.deliverypersonservice.controllers;

import com.example.deliverypersonservice.dtos.DeliveryPersonUpdateDto;
import com.example.deliverypersonservice.services.DeliveryPersonService;
import com.example.sharedfilesmodule.dtos.deliveryperson.DeliveryPersonResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/delivery-person")
@RequiredArgsConstructor
public class DeliveryPersonController {
    private static final String HEADER_NAME="X-User-Id";
    private final DeliveryPersonService deliveryPersonService;


    @GetMapping("/{id}")
    public ResponseEntity<DeliveryPersonResponseDto> getDeliveryPerson(@PathVariable UUID id){
        return ResponseEntity.ok(deliveryPersonService.getDeliveryPerson(id));
    }
    @GetMapping
    public ResponseEntity<List<DeliveryPersonResponseDto>> getAllDeliveryPeople(){
        return ResponseEntity.ok(deliveryPersonService.getAllDeliveryPerson());
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteDeliveryPerson(@RequestHeader(HEADER_NAME) UUID id){
        deliveryPersonService.deleteDeliveryPerson(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping
    public ResponseEntity<DeliveryPersonResponseDto> updateDeliveryPerson(@RequestHeader(HEADER_NAME) UUID id, @RequestBody @Valid DeliveryPersonUpdateDto deliveryPersonUpdateDto){
        return ResponseEntity.ok(deliveryPersonService.updateDeliveryPerson(id, deliveryPersonUpdateDto));
    }
}
