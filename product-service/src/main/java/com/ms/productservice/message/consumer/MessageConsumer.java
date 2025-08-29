package com.ms.productservice.message.consumer;


import com.example.sharedfilesmodule.dtos.StockValidationRequestDto;
import com.example.sharedfilesmodule.dtos.StockValidationResponseDto;
import com.ms.productservice.exceptions.ProductNotFoundException;
import com.ms.productservice.repositories.ProductRepository;
import com.ms.shared.dtos.stock.StockUpdateMessage;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class MessageConsumer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProductRepository productRepository;
    public MessageConsumer(KafkaTemplate<String, Object> kafkaTemplate, ProductRepository productRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "order-validate-stock", groupId = "product-service")
    public void validateStock(StockValidationRequestDto requestDto) {
        try {
            BigDecimal totalPrice = BigDecimal.ZERO;
            boolean hasStock = true;
            for (var item : requestDto.items()) {
                var product = productRepository.findById(item.productId()).orElseThrow(ProductNotFoundException::new);

                totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(item.quantity())));
                if (product.getQuantity() < item.quantity()) {
                    hasStock = false;
                    break;
                }
            }
            StockValidationResponseDto responseDto = new StockValidationResponseDto(requestDto.orderId(), hasStock ? "VALID" : "INVALID", totalPrice);
            String targetTopic = hasStock ? "stock-valid-topic" : "stock-invalid-topic";
            kafkaTemplate.send(targetTopic, responseDto);
        } catch (ProductNotFoundException e){
            log.error("Product not found during validation for order: {}", requestDto.orderId());
            var response = new StockValidationResponseDto(requestDto.orderId(), "INVALID", BigDecimal.ZERO);
            kafkaTemplate.send("stock-invalid-topic", response);
        }
    }
    @KafkaListener(topics = "order-stock-update", groupId = "product-service")
    @Transactional
    public void updateStock(StockUpdateMessage message) {

            message.items().forEach(stockItemDto -> {
                try {
                var product = productRepository.findById(stockItemDto.productId()).orElseThrow(ProductNotFoundException::new);
                product.setQuantity(product.getQuantity() - stockItemDto.quantity());
                productRepository.save(product);
            } catch (ProductNotFoundException e){
                log.error("Product: {} not found during update",stockItemDto.productId() );
            }
            });
    }


}
