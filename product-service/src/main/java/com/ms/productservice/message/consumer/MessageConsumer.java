package com.ms.productservice.message.consumer;


import com.example.sharedfilesmodule.dtos.StockUpdateMessage;
import com.example.sharedfilesmodule.dtos.StockValidationRequestDto;
import com.ms.productservice.exceptions.ProductNotFoundException;
import com.ms.productservice.repositories.ProductRepository;
import com.ms.productservice.services.ProductStockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer {
    private final ProductRepository productRepository;
    private final ProductStockService productInternalService;
    @KafkaListener(topics = "order.validate-stock", groupId = "product-service")
    public void validateStock(StockValidationRequestDto requestDto) {
        productInternalService.validateStockRequest(requestDto);
    }
    @KafkaListener(topics = "order.stock-update", groupId = "product-service")
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
