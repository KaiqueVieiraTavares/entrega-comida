package com.ms.orderservice.messaging.producer.order_product;

import com.ms.shared.dtos.stock.StockItemDto;
import com.ms.shared.dtos.stock.StockUpdateMessage;
import com.ms.shared.dtos.stock.StockValidationRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OrderMessagingProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendStockValidationRequest(StockValidationRequestDto stockValidationRequestDto) {
        var future = kafkaTemplate.send("order.validate-stock", stockValidationRequestDto);

        future.whenComplete((sendResult, ex) -> {
            if(ex != null){
                log.error("error sending stock validation request to product-service", ex);
            } else{
                log.info("stock validation request sent successfully! Topic: {}, Partition: {}, Offset: {} ",
                        sendResult.getRecordMetadata().topic(),
                        sendResult.getRecordMetadata().partition(),
                        sendResult.getRecordMetadata().offset());
            }
        });
    }

    public void sendStockUpdate(List<StockItemDto> items) {
        var message = new StockUpdateMessage(items);
        var future = kafkaTemplate.send("order-update-stock", message);

        future.whenComplete((sendResult, ex) -> {
            if(ex != null){
                log.error("Error sending stock update request to product-service", ex);
            } else{
                log.info("stock update request sent successfully! Topic: {}, Partition: {}, Offset: {}",
                        sendResult.getRecordMetadata().topic(),
                        sendResult.getRecordMetadata().partition(),
                        sendResult.getRecordMetadata().offset());
            }
        });
    }
}
