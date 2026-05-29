package com.ms.orderservice.services;

import com.example.sharedfilesmodule.dtos.StockItemDto;
import com.example.sharedfilesmodule.dtos.StockValidationRequestDto;
import com.example.sharedfilesmodule.enums.OrderStatus;
import com.ms.orderservice.dtos.CreateOrderResponseDto;
import com.ms.orderservice.dtos.OrderRequestDto;
import com.ms.orderservice.dtos.OrderResponseDto;
import com.ms.orderservice.entities.OrderEntity;
import com.ms.orderservice.entities.OrderItemEntity;
import com.ms.orderservice.exceptions.KafkaSendException;
import com.ms.orderservice.exceptions.OrderNotFoundException;
import com.ms.orderservice.exceptions.UnauthorizedAccessException;
import com.ms.orderservice.messaging.producer.order.OrderConfirmedProducer;
import com.ms.orderservice.messaging.producer.product.ProductStockValidationProducer;
import com.ms.orderservice.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
public class OrderService {
    private final ProductStockValidationProducer productStockValidationProducer;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final OrderConfirmedProducer orderConfirmedProducer;

    public OrderService(ProductStockValidationProducer productStockValidationProducer, OrderRepository orderRepository, ModelMapper modelMapper, OrderConfirmedProducer orderConfirmedProducer) {
        this.productStockValidationProducer = productStockValidationProducer;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;

        this.orderConfirmedProducer = orderConfirmedProducer;
    }
    @Transactional
    public CreateOrderResponseDto createOrder(UUID userId, OrderRequestDto orderRequestDto){
        try {
            UUID orderId = UUID.randomUUID();
            var orderEntity = new OrderEntity();
            orderEntity.setUserId(userId);
            orderEntity.setId(orderId);
            orderEntity.setStatus(OrderStatus.PENDING_VALIDATION);
            orderRepository.save(orderEntity);
            List<StockItemDto> items = orderRequestDto.items().stream().map(item ->
                            new StockItemDto(item.productId(), item.quantity()))
                    .toList();
            StockValidationRequestDto validation = new StockValidationRequestDto(orderId, userId, items);

            productStockValidationProducer.sendStockValidationRequest(validation);

            return new CreateOrderResponseDto(orderId, "Request received, awaiting validation");
        }catch(Exception e){
            log.error("Error creating order for user: {}" ,userId, e);
            throw new RuntimeException("");
        }

    }

    public OrderResponseDto getOrder(UUID userId, UUID orderId) {
        var order =  orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if(!(order.getUserId().equals(userId))){
            throw new UnauthorizedAccessException();
        }
        return modelMapper.map(order, OrderResponseDto.class);

    }
    public List<OrderResponseDto> getAllOrders() {
        var orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponseDto.class))
                .toList();
    }
    @Transactional
    public void deleteOrder(UUID userId, UUID orderId){
        var order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if(!(order.getUserId().equals(userId))){
            throw new UnauthorizedAccessException();
        }
        orderRepository.delete(order);
    }

    @Transactional
    public OrderResponseDto updateOrder(UUID userId, UUID orderId, OrderRequestDto orderRequestDto){
        var order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if(!(order.getUserId().equals(userId))){
            throw new UnauthorizedAccessException();
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
    @Transactional
    public OrderResponseDto confirmOrder(UUID userId, UUID orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (!(order.getUserId().equals(userId))) {
            throw new UnauthorizedAccessException();
        }

        order.setStatus(OrderStatus.PAID);
        var savedOrder = orderRepository.save(order);

        try {
            orderConfirmedProducer.sendOrderConfirmed(savedOrder.getUserId(), savedOrder.getId(), savedOrder.getRestaurantId());
        } catch (KafkaSendException e) {
            log.error("Error sending order confirmation to Kafka. OrderId: {}", savedOrder.getId(), e);

        }

        return modelMapper.map(savedOrder, OrderResponseDto.class);
    }
}
