package com.example.deliverypersonservice.templates;

import com.example.deliverypersonservice.dtos.DeliveryPersonResponseDto;
import com.example.deliverypersonservice.dtos.DeliveryPersonUpdateDto;
import com.example.deliverypersonservice.entities.DeliveryPersonEntity;
import com.example.sharedfilesmodule.dtos.deliveryperson.DeliveryPersonCreatedEvent;
import com.example.sharedfilesmodule.enums.VehicleType;

import java.util.UUID;

public class DeliveryPersonTemplateFactory {

    public static final UUID ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static final String NAME = "Entregador Teste";
    public static final String CNH = "12345678901";
    public static final String PLATE = "ABC-1234";
    public static final VehicleType VEHICLE_TYPE = VehicleType.MOTORCYCLE;
    public static final String EMAIL = "teste@email.com";
    public static final String TOKEN = "mock-token";

    public static DeliveryPersonEntity createValidDeliveryPersonEntity() {
        return DeliveryPersonEntity.builder()
                .id(ID)
                .name(NAME)
                .cnh(CNH)
                .vehiclePlate(PLATE)
                .vehicleType(VEHICLE_TYPE)
                .available(true)
                .build();
    }

    public static DeliveryPersonUpdateDto createValidDeliveryPersonUpdateDto() {
        return new DeliveryPersonUpdateDto(
                NAME + " Alterado",
                "98765432100",
                "XYZ-9876",
                VehicleType.CAR
        );
    }

    public static DeliveryPersonResponseDto createValidDeliveryPersonResponseDto() {
        return new DeliveryPersonResponseDto(EMAIL, "hashed-password", TOKEN);
    }

    public static DeliveryPersonCreatedEvent createValidDeliveryPersonCreatedEvent() {
        return new DeliveryPersonCreatedEvent(
                UUID.randomUUID(),
                NAME,
                CNH,
                PLATE,
                VEHICLE_TYPE
        );
    }
}