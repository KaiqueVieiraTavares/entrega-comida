package com.ms.authenticationservice.services.delivery_person;

import com.example.sharedfilesmodule.enums.Role;
import com.ms.authenticationservice.clients.DeliveryPersonServiceClient;
import com.ms.authenticationservice.dtos.delivery_person.DeliveryPersonRegisterDTO;
import com.ms.authenticationservice.exceptions.deliveryPerson.VehiclePlateAlreadyExists;
import com.ms.authenticationservice.mapper.DeliveryPersonMapper;
import com.ms.authenticationservice.messaging.DeliveryPersonEventProducer;
import com.ms.authenticationservice.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryPersonService {
    private final AuthService authService;
    private final DeliveryPersonEventProducer deliveryPersonEventProducer;
    private final DeliveryPersonServiceClient deliveryPersonServiceClient;
    private final DeliveryPersonMapper deliveryPersonMapper;

    @Transactional
    public UUID registerDeliveryPerson(DeliveryPersonRegisterDTO deliveryPersonRequestDTO){
        if(deliveryPersonServiceClient.existsByVehiclePlate(deliveryPersonRequestDTO.vehiclePlate())){
            throw new VehiclePlateAlreadyExists();
        }
        var savedDeliveryperson = authService.createAuthCredentials(deliveryPersonRequestDTO.email(), deliveryPersonRequestDTO.password(), Role.DELIVERY_PERSON);
        deliveryPersonEventProducer.publishDeliveryPersonCreated(deliveryPersonMapper.toDeliveryPersonCreatedEvent(savedDeliveryperson.getId(), deliveryPersonRequestDTO));
        return savedDeliveryperson.getId();
    }

}
