package com.ms.orderservice.messaging.producer;


import com.ms.orderservice.exceptions.KafkaSendException;
import com.ms.shared.dtos.stock.StockItemDto;
import com.ms.shared.dtos.stock.StockUpdateMessage;
import com.ms.shared.dtos.stock.StockValidationRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.SendResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class OrderMessagingProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendStockValidationRequest(StockValidationRequestDto stockValidationRequestDto) {
        try{
            SendResult<String,Object> result = kafkaTemplate.send("order.validate-stock", stockValidationRequestDto).get(10, TimeUnit.SECONDS);
            log.info("Message sent sucessfully! Topic: {}, Partition: {}, Ofsset: {} ",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
        } catch (InterruptedException| ExecutionException| TimeoutException e ){
            log.error("Critical failure sending message to Kafka after automatic retries: {}", e.getMessage());
            throw new KafkaSendException("Failed to process inventory validation. Please try again");
        }
    }

    public void sendStockUpdate(List<StockItemDto> items) {
        var message = new StockUpdateMessage(items);
        try {
            SendResult<String, Object> result = kafkaTemplate.send("order-update-stock", message).get(10, TimeUnit.SECONDS);
            log.info("Message sent successfully! Topic: {}, Partition: {}, Offset: {} ", result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(), result.getRecordMetadata().offset());

        } catch (InterruptedException| ExecutionException | TimeoutException e) {
            log.error("Critical failure sending message to Kafka after automatic retries: {}", e.getMessage());
            throw new KafkaSendException("Failed to process inventory update. Please try again");
        }
    }


}
