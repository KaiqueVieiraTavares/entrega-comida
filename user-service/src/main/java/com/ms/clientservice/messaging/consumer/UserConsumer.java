package com.ms.clientservice.messaging.consumer;

import com.example.sharedfilesmodule.dtos.user.UserCreatedEvent;
import com.ms.clientservice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserConsumer {
    private final UserService userService;
    private static final String TOPIC_NAME="user-created";
    private static final String GROUP_ID_NAME="user-service";
    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID_NAME)
    public void onUserCreated(UserCreatedEvent userCreatedEvent) {
        try {
            log.info("Received user-created event: {}", userCreatedEvent);
            userService.registerUser(userCreatedEvent);
        } catch (Exception e) {
            log.error("Error processing user-created event", e);
            throw e;
        }
    }
}
