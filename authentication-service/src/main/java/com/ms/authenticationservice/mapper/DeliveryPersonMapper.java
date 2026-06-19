package com.ms.authenticationservice.mapper;

import com.example.sharedfilesmodule.dtos.deliveryperson.DeliveryPersonCreatedEvent;
import com.ms.authenticationservice.dtos.delivery_person.DeliveryPersonRegisterDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeliveryPersonMapper {

    public DeliveryPersonCreatedEvent toDeliveryPersonCreatedEvent(
            UUID authId,
            DeliveryPersonRegisterDTO dto) {

        return new DeliveryPersonCreatedEvent(
                authId,
                dto.name(),
                dto.cnh(),
                dto.vehiclePlate(),
                dto.vehicleType()
        );
    }
}
