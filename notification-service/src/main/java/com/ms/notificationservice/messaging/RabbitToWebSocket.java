package com.ms.notificationservice.messaging;

import com.ms.notificationservice.configurations.RabbitmqConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitToWebSocket {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public RabbitToWebSocket(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @RabbitListener(queues = RabbitmqConfiguration.qeues)
    public void consumeFromRabbit(Object message){

    }
}
