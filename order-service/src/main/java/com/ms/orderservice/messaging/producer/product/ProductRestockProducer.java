package com.ms.orderservice.messaging.producer.product;

import com.example.sharedfilesmodule.dtos.StockUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductRestockProducer {
    private final KafkaTemplate<String, StockUpdateMessage> kafkaTemplate;


    public void sendStockRestore(StockUpdateMessage stockUpdateMessage){
        var future = kafkaTemplate.send("order.restore-stock", stockUpdateMessage);
        future.whenComplete((sendResult, ex) ->{
            if(ex!=null){
                log.error("error sending restock request to product-service", ex);
            } else{
                log.info("restock request sent successfully! Topic: {}, Partition: {}, Offset: {} ",
                        sendResult.getRecordMetadata().topic(),
                        sendResult.getRecordMetadata().partition(),
                        sendResult.getRecordMetadata().offset());
            }});
    }
}



