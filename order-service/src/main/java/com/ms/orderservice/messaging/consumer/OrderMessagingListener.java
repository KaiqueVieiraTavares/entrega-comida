package com.ms.orderservice.messaging.consumer;


import com.ms.orderservice.dtos.StockValidationRequestDto;
import com.ms.orderservice.dtos.StockValidationResponseDto;
import com.ms.orderservice.entities.OrderEntity;
import com.ms.orderservice.enums.OrderStatus;
import com.ms.orderservice.repositories.OrderRepository;
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

        OrderEntity order = new OrderEntity();
        order.setId(stockValidationResponseDto.orderId());
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setTotalPrice(stockValidationResponseDto.totalPrice());
        orderRepository.save(order);
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
