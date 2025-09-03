package com.ms.authenticationservice.services;

import com.example.sharedfilesmodule.dtos.deliveryperson.DeliveryPersonRequestDTO;

import com.ms.authenticationservice.clients.DeliveryPersonServiceClient;
import com.ms.authenticationservice.dtos.deliveryPerson.DeliveryPersonLoginDto;
import com.ms.authenticationservice.dtos.deliveryPerson.DeliverypersonResponseLoginDto;
import com.ms.authenticationservice.exceptions.deliveryPerson.DeliveryPersonEmailAlreadyExistsException;
import com.ms.authenticationservice.exceptions.deliveryPerson.VehiclePlateAlreadyExists;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthDeliveryPersonService {
    private final PasswordEncoder passwordEncoder;
    private final DeliveryPersonServiceClient deliveryPersonServiceClient;
    private final TokenService tokenService;
    public AuthDeliveryPersonService(PasswordEncoder passwordEncoder, DeliveryPersonServiceClient deliveryPersonServiceClient, TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.deliveryPersonServiceClient = deliveryPersonServiceClient;
        this.tokenService = tokenService;
    }
    public void registerDeliveryPerson(DeliveryPersonRequestDTO deliveryPersonRequestDTO){
        if(deliveryPersonServiceClient.existsByEmail(deliveryPersonRequestDTO.email())){
            throw new DeliveryPersonEmailAlreadyExistsException();
        }
        if(deliveryPersonServiceClient.existsByVehiclePlate(deliveryPersonRequestDTO.vehiclePlate())){
            throw new VehiclePlateAlreadyExists();
        }
        deliveryPersonServiceClient.registerDeliveryPerson(deliveryPersonRequestDTO);
    }

    public DeliverypersonResponseLoginDto loginDeliveryPerson(DeliveryPersonLoginDto deliveryPersonLoginDto){
        var savedDeliveryPerson = deliveryPersonServiceClient.findByEmail(deliveryPersonLoginDto.email());
        if(!(passwordEncoder.matches(deliveryPersonLoginDto.password(), savedDeliveryPerson.hashPassword()))){
            throw new BadCredentialsException("invalid password");
        }
        var token = tokenService.generateToken(savedDeliveryPerson.email());
        return new DeliverypersonResponseLoginDto(savedDeliveryPerson.email(), token);
    }

}
