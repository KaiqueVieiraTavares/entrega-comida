package com.ms.notificationservice.configurations;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitmqConfiguration {
    private static final String QUEUE = "notification-queue";
    private static final String EXCHANGE= "notification-exchange";
    private static final String ROUTING_KEY = "notification.routing.key";


    @Bean
    public Queue queue(){
        return QueueBuilder.durable(QUEUE).build();
    }
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
