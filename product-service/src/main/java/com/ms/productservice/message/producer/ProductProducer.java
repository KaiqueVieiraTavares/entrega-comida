package com.ms.productservice.message.producer;

import com.example.sharedfilesmodule.dtos.StockValidationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductProducer {
    private final KafkaTemplate<String, StockValidationResponseDto> kafkaTemplate;
    private static final String VALIDATION_RESULT = "order.stock-validation-result";
    public void sendValidationResult(StockValidationResponseDto stockValidationResponseDto){
        kafkaTemplate.send(VALIDATION_RESULT, stockValidationResponseDto);
    }
}
