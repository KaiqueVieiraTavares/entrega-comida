package com.ms.orderservice.messaging.producer.order_product;


import com.example.sharedfilesmodule.dtos.StockUpdateMessage;
import com.example.sharedfilesmodule.dtos.StockValidationRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class OrderMessagingProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendStockValidationRequest(StockValidationRequestDto stockValidationRequestDto) {
        var future = kafkaTemplate.send("order.validate-stock", stockValidationRequestDto);
       buildFuture(future);
    }

    public void sendStockRestore(StockUpdateMessage stockUpdateMessage){
        var future = kafkaTemplate.send("order.restore-stock", stockUpdateMessage);
        buildFuture(future);
    }
    private void buildFuture(CompletableFuture<? extends SendResult<String, ?>> future){
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
}
