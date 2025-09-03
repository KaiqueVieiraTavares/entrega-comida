package com.ms.authenticationservice.controllers;

import com.example.sharedfilesmodule.dtos.deliveryperson.DeliveryPersonRequestDTO;
import com.ms.authenticationservice.dtos.deliveryPerson.DeliveryPersonLoginDto;
import com.ms.authenticationservice.dtos.deliveryPerson.DeliverypersonResponseLoginDto;
import com.ms.authenticationservice.services.AuthDeliveryPersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/delivery-person")
public class AuthDeliverypersonController {
    private final AuthDeliveryPersonService authDeliveryPersonService;

    public AuthDeliverypersonController(AuthDeliveryPersonService authDeliveryPersonService) {
        this.authDeliveryPersonService = authDeliveryPersonService;
    }
    public ResponseEntity<Void> registerDeliveryPerson(DeliveryPersonRequestDTO deliveryPersonRequestDTO){
        authDeliveryPersonService.registerDeliveryPerson(deliveryPersonRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    public ResponseEntity<DeliverypersonResponseLoginDto> loginDeliveryPerson(DeliveryPersonLoginDto deliveryPersonLoginDto){
        var response = authDeliveryPersonService.loginDeliveryPerson(deliveryPersonLoginDto);
        return ResponseEntity.ok(response);
    }
}
