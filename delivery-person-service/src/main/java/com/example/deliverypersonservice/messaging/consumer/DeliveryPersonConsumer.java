package com.example.deliverypersonservice.messaging.consumer;

import com.example.deliverypersonservice.services.DeliveryPersonService;
import com.example.sharedfilesmodule.dtos.deliveryperson.DeliveryPersonCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryPersonConsumer {
    private final DeliveryPersonService deliveryPersonService;


    @KafkaListener(topics = "delivery_person-created", groupId = "delivery-person")
    public void onDeliveryPersonCreated(DeliveryPersonCreatedEvent deliveryPersonCreatedEvent){
        deliveryPersonService.createDeliveryPerson(deliveryPersonCreatedEvent);
    }
}
