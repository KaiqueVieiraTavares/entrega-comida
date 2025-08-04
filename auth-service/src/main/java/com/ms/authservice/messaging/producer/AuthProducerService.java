package com.ms.authservice.messaging.producer;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthProducerService {
    private final KafkaTemplate<String, Object>;

}
