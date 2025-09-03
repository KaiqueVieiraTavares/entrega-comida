package com.ms.authenticationservice.clients;

import com.example.sharedfilesmodule.dtos.deliveryperson.DeliveryPersonRequestDTO;
import com.example.sharedfilesmodule.dtos.deliveryperson.DeliveryPersonResponseDto;
import com.example.sharedfilesmodule.dtos.user.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
@FeignClient(name = "delivery-person-service")
public interface DeliveryPersonServiceClient {
    @GetMapping("/delivery-person/exists/{plate}")
    Boolean existsByVehiclePlate(@PathVariable String plate);

    @GetMapping("/delivery-person/exists/{email}")
    Boolean existsByEmail(@PathVariable String email);

    @PostMapping("/delivery-person")
    void registerDeliveryPerson(@RequestBody @Valid DeliveryPersonRequestDTO deliveryPersonRequestDTO);


    @GetMapping("/delivery-person/{email}")
    DeliveryPersonResponseDto findByEmail(@PathVariable String email);
}
