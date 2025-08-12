package com.ms.productservice.message.consumer;


import com.ms.productservice.exceptions.ProductNotFoundException;
import com.ms.productservice.repositories.ProductRepository;
import com.ms.shared.dtos.stock.StockUpdateMessage;
import com.ms.shared.dtos.stock.StockValidationRequestDto;
import com.ms.shared.dtos.stock.StockValidationResponseDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MessageConsumer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProductRepository productRepository;
    public MessageConsumer(KafkaTemplate<String, Object> kafkaTemplate, ProductRepository productRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "order-validate-stock", groupId = "product-service")
    public void validateStock(StockValidationRequestDto requestDto){
        BigDecimal totalPrice = BigDecimal.ZERO;
        boolean hasStock = true;
        for(var item: requestDto.items()){
            var product = productRepository.findById(item.productId()).orElseThrow(() -> new ProductNotFoundException());

            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(item.quantity())));
            if(product.getQuantity() < item.quantity()){
                hasStock = false;
                break;
            }
        }

        StockValidationResponseDto responseDto = new StockValidationResponseDto(
                requestDto.orderId(),
                hasStock ? "APPROVED" : "REJECTED",
                totalPrice
        );
        kafkaTemplate.send("stock-validation-result", responseDto);
    }
    @KafkaListener(topics = "order-stock-update", groupId = "product-service")
    public void updateStock(StockUpdateMessage message){
        message.items().stream().forEach(stockItemDto -> {
            var product = productRepository.findById(stockItemDto.productId()).orElseThrow(() -> new ProductNotFoundException());
            product.setQuantity(product.getQuantity() - stockItemDto.quantity());
            productRepository.save(product);
        });
    }

}
