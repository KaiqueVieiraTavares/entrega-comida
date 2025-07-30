package com.ms.orderservice.services;

import com.ms.orderservice.configuration.ModelMapperConfiguration;
import com.ms.orderservice.dtos.*;
import com.ms.orderservice.entities.OrderEntity;
import com.ms.orderservice.entities.OrderItemEntity;
import com.ms.orderservice.enums.OrderStatus;
import com.ms.orderservice.exceptions.OrderNotFoundException;
import com.ms.orderservice.exceptions.UnauthorizedAccessException;
import com.ms.orderservice.messaging.producer.OrderMessagingProducer;
import com.ms.orderservice.repositories.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderMessagingProducer orderMessagingProducer;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final DefaultKafkaConsumerFactory defaultKafkaConsumerFactory;

    public OrderService(OrderMessagingProducer orderMessagingProducer, OrderRepository orderRepository, ModelMapper modelMapper, DefaultKafkaConsumerFactory defaultKafkaConsumerFactory) {
        this.orderMessagingProducer = orderMessagingProducer;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.defaultKafkaConsumerFactory = defaultKafkaConsumerFactory;
    }

    public CreateOrderResponseDto createOrder(UUID clientId, OrderRequestDto orderRequestDto){
        UUID orderId = UUID.randomUUID();
        List<StockItemDto> items = orderRequestDto.items().stream().map(item ->
                new StockItemDto(item.productId(), item.quantity()))
                .toList();
        StockValidationRequestDto validation = new StockValidationRequestDto(orderId, clientId, items);

        orderMessagingProducer.sendStockValidationRequest(validation);

        return new CreateOrderResponseDto(orderId, "Request received, awaiting validation");
    }

    public OrderResponseDto getOrder(UUID clientId, UUID orderId) {
        var order =  orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if(!(order.getClientId().equals(clientId))){
            throw new UnauthorizedAccessException("You do not have access to this content");
        }
        return modelMapper.map(order, OrderResponseDto.class);

    }
    public List<OrderResponseDto> getAllOrders() {
        var orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponseDto.class))
                .toList();
    }

    public void deleteOrder(UUID clientId, UUID orderId){
        var order = orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException("Order not found"));
        if(!(order.getClientId().equals(clientId))){
            throw new UnauthorizedAccessException("You do not have access to this content");
        }
        orderRepository.delete(order);
    }


    public OrderResponseDto updateOrder(UUID clientId, UUID orderId, OrderRequestDto orderRequestDto){
        var order = orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException("Order not found"));
        if(!(order.getClientId().equals(clientId))){
            throw new UnauthorizedAccessException("You do not have access to this content");
        }
        if(orderRequestDto.items() == null || orderRequestDto.items().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }
        order.getItems().clear();
        orderRequestDto.items().forEach(itemDto -> {
            OrderItemEntity item = new OrderItemEntity();
            item.setProductId(itemDto.productId());
            item.setName(itemDto.name());
            item.setPrice(itemDto.price());
            item.setQuantity(itemDto.quantity());
            order.addItem(item);
        });
        return modelMapper.map(orderRepository.save(order), OrderResponseDto.class);
    }

    public OrderResponseDto confirmOrder(UUID clientId, UUID orderId){
        var order=orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if(!(order.getClientId().equals(clientId))){
            throw new UnauthorizedAccessException("You do not have access to this content");
        }
        orderMessagingProducer.sendStockUpdate(order.getItems().stream().map(
                itemEntity -> new StockItemDto(itemEntity.getProductId(),
                        itemEntity.getQuantity())
        ).toList());
        order.setStatus(OrderStatus.PAID);
        return modelMapper.map(orderRepository.save(order), OrderResponseDto.class);
    }
}
