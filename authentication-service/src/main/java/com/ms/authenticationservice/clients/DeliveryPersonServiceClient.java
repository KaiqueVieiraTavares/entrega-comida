package com.ms.authenticationservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(name = "delivery-person-service")
public interface DeliveryPersonServiceClient {
    @GetMapping("/delivery-person/exists/{plate}")
    Boolean existsByVehiclePlate(@PathVariable String plate);
}
