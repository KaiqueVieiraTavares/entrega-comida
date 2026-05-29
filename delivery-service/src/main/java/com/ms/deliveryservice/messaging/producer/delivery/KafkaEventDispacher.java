package com.ms.deliveryservice.messaging.producer.delivery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventDispacher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String topic, String key, Object object){
        kafkaTemplate.send(topic, key, object).whenComplete((sendResult, throwable) -> {
            if(throwable!=null){
                log.error("Failed to send message to topic: {}, error: {}", topic,throwable.getMessage());
            } else{
                var metaData = sendResult.getRecordMetadata();
                log.info("Message sent successfully! Topic: {}, Partition: {}, Offset: {}",
                        metaData.topic(),
                        metaData.partition(),
                        metaData.offset());
            }
        });
    }
}
