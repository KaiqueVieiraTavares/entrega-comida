package com.ms.orderservice.messaging.producer;


import com.ms.orderservice.dtos.StockItemDto;
import com.ms.orderservice.dtos.StockUpdateMessage;
import com.ms.orderservice.dtos.StockValidationRequestDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.SendResult;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class OrderMessagingProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderMessagingProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendStockValidationRequest(StockValidationRequestDto stockValidationRequestDto){
        kafkaTemplate.send("order.validate-stock", stockValidationRequestDto);
    }

    public void sendStockUpdate(List<StockItemDto> items) {
        var message = new StockUpdateMessage(items);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("order-update-stock", message);

        future.thenAccept(sendResult -> {
            System.out.println("Mensagem enviada com sucesso: " + sendResult.getRecordMetadata());
        }).exceptionally(ex -> {
            System.err.println("Erro ao enviar mensagem: " + ex.getMessage());
            return null;
        });
    }


}
