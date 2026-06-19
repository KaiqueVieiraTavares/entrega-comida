package com.ms.authenticationservice.messaging;

import com.example.sharedfilesmodule.dtos.deliveryperson.DeliveryPersonCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeliveryPersonEventProducer {
    private final KafkaTemplate<String, DeliveryPersonCreatedEvent> kafkaTemplate;
    private static final String DELIVERY_PERSON_CREATED_TOPIC="delivery-person-created";

    public void publishDeliveryPersonCreated(DeliveryPersonCreatedEvent deliveryPersonCreatedEvent){
        kafkaTemplate.send(DELIVERY_PERSON_CREATED_TOPIC, deliveryPersonCreatedEvent);
    }
}
