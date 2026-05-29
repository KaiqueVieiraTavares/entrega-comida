package com.ms.orderservice.messaging.producer.product;


import com.example.sharedfilesmodule.dtos.StockValidationRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class ProductStockValidationProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProductStockValidationProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendStockValidationRequest(StockValidationRequestDto stockValidationRequestDto) {
        var future = kafkaTemplate.send("order.validate-stock", stockValidationRequestDto);
         future.whenComplete((sendResult, ex) ->{
            if(ex!=null){
                log.error("error sending stock validation request to product-service", ex);
            } else{
                log.info("stock validation request sent successfully! Topic: {}, Partition: {}, Offset: {} ",
                        sendResult.getRecordMetadata().topic(),
                        sendResult.getRecordMetadata().partition(),
                        sendResult.getRecordMetadata().offset());
            }});
    }


    private void buildFuture(CompletableFuture<? extends SendResult<String, ?>> future){

    }
}
