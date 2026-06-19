package com.ms.authenticationservice.messaging;

import com.example.sharedfilesmodule.dtos.user.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventProducer {
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;
    private static final String USER_CREATED_TOPIC="user-created";

    public void publishUserCreatedEvent(UserCreatedEvent userCreatedEvent){
        kafkaTemplate.send(USER_CREATED_TOPIC, userCreatedEvent);
    }
}
