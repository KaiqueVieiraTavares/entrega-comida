package com.ms.notificationservice.messaging;

import com.ms.notificationservice.configurations.RabbitmqConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaToRabbit {
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;
    public KafkaToRabbit(RabbitTemplate rabbitTemplate,
                         @Value("${rabbitmq.exchange}") String exchange,
                         @Value("${rabbitmq.routing-key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @KafkaListener()
    public void consumerOrderConfirmationEvent(Object orderConfirmation){
        rabbitTemplate.convertAndSend(exchange, routingKey, )
    }
}
