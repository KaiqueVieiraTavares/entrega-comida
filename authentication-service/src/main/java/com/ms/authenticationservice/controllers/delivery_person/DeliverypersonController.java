package com.ms.authenticationservice.controllers.delivery_person;

import com.ms.authenticationservice.dtos.delivery_person.DeliveryPersonRegisterDTO;
import com.ms.authenticationservice.services.delivery_person.DeliveryPersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/delivery-person")
@RequiredArgsConstructor
public class DeliverypersonController {
    private final DeliveryPersonService deliveryPersonService;

    @PostMapping("/register")
    public ResponseEntity<UUID> registerDeliveryPerson(@RequestBody @Valid DeliveryPersonRegisterDTO deliveryPersonRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryPersonService.registerDeliveryPerson(deliveryPersonRequestDTO));
    }

}
