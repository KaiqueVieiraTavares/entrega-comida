package com.ms.orderservice.messaging.producer;


import com.ms.shared.dtos.stock.StockItemDto;
import com.ms.shared.dtos.stock.StockUpdateMessage;
import com.ms.shared.dtos.stock.StockValidationRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.SendResult;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class OrderMessagingProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendStockValidationRequest(StockValidationRequestDto stockValidationRequestDto){
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("order.validate-stock", stockValidationRequestDto);

            future.thenAccept(sendResult -> {
                log.info("Stock validation request sent successfully for order: {} ", stockValidationRequestDto.orderId());
            }).exceptionally(ex -> {
                log.error("Error sending stock validation for order: {}", stockValidationRequestDto.orderId(), ex);
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendStockUpdate(List<StockItemDto> items) {
        var message = new StockUpdateMessage(items);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("order-update-stock", message);

        future.thenAccept(sendResult -> {
            log.info("Mensagem enviada com sucesso!");
            log.info("{}", sendResult.getRecordMetadata());
        }).exceptionally(ex -> {
            log.error("Error sending stock update: {}", ex.getMessage());
            return null;
        });
    }


}
