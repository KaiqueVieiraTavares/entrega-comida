package com.ms.orderservice.messaging.consumer;


import com.example.sharedfilesmodule.enums.OrderStatus;
import com.ms.orderservice.entities.OrderEntity;
import com.ms.orderservice.repositories.OrderRepository;
import com.ms.shared.dtos.stock.StockValidationResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Slf4j
@Component
public class OrderMessagingListener {

    private final OrderRepository orderRepository;

    public OrderMessagingListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "stock-valid-topic", groupId = "order-service")
    public void handleStockIsValid(StockValidationResponseDto stockValidationResponseDto){
        Optional<OrderEntity> orderEntity = orderRepository.findById(stockValidationResponseDto.orderId());
        if(orderEntity.isPresent()){
          var order = orderEntity.get();
          order.setStatus(OrderStatus.PENDING_PAYMENT);
          order.setTotalPrice(stockValidationResponseDto.totalPrice());
          orderRepository.save(order);
        } else{
            log.error("Order with id: {} not found to validate", stockValidationResponseDto.orderId());
        }
    }

    @KafkaListener(topics = "stock-invalid-topic", groupId = "order-service")
    public void handleStockIsNotValid(StockValidationResponseDto stockValidationResponseDto){
        Optional<OrderEntity> optionalOrder = orderRepository.findById(stockValidationResponseDto.orderId());

        if(optionalOrder.isPresent()){
            OrderEntity order = optionalOrder.get();
            order.setStatus(OrderStatus.CANCELED);

            orderRepository.save(order);
        } else{
            log.error("Order with id {} not found to cancel ", stockValidationResponseDto.orderId());
        }
    }
}
