package com.ms.orderservice.services;

import com.example.sharedfilesmodule.dtos.OrderConfirmedDtoDelivery;
import com.example.sharedfilesmodule.enums.OrderStatus;
import com.ms.orderservice.dtos.*;

import com.ms.orderservice.entities.OrderEntity;
import com.ms.orderservice.entities.OrderItemEntity;
import com.ms.orderservice.exceptions.BusinessException;
import com.ms.orderservice.exceptions.KafkaSendException;
import com.ms.orderservice.exceptions.OrderNotFoundException;
import com.ms.orderservice.exceptions.UnauthorizedAccessException;
import com.ms.orderservice.messaging.producer.DeliveryMessagingProducer;
import com.ms.orderservice.messaging.producer.OrderMessagingProducer;
import com.ms.orderservice.repositories.OrderRepository;
import com.ms.shared.dtos.stock.StockItemDto;
import com.ms.shared.dtos.stock.StockValidationRequestDto;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
public class OrderService {
    private final OrderMessagingProducer orderMessagingProducer;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final DeliveryMessagingProducer deliveryMessagingProducer;

    public OrderService(OrderMessagingProducer orderMessagingProducer, OrderRepository orderRepository, ModelMapper modelMapper, DeliveryMessagingProducer deliveryMessagingProducer) {
        this.orderMessagingProducer = orderMessagingProducer;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;

        this.deliveryMessagingProducer = deliveryMessagingProducer;
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

            orderMessagingProducer.sendStockValidationRequest(validation);

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
        try {
            orderMessagingProducer.sendStockUpdate(order.getItems().stream().map(
                    itemEntity -> new StockItemDto(itemEntity.getProductId(),
                            itemEntity.getQuantity())
            ).toList());
            order.setStatus(OrderStatus.PAID);
            var savedOrder = orderRepository.save(order);
            try{
                deliveryMessagingProducer.sendOrderConfirmed(savedOrder.getUserId(), savedOrder.getId(), savedOrder.getRestaurantId());
            } catch (KafkaSendException e){
                log.error("Error sending order confirmation to Kafka. OrderId: {}", savedOrder.getId(), e);
            }
            return modelMapper.map(savedOrder, OrderResponseDto.class);
        } catch (KafkaSendException e){
            order.setStatus(OrderStatus.PENDING_SYNC);
            orderRepository.save(order);
            throw new BusinessException("Order confirmed, but inventory synchronization is pending");
        }
    }

    @Transactional
    public OrderResponseDto retryOrderSync(UUID userId, UUID orderId){
        var order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if(!(order.getUserId().equals(userId))){
            throw new UnauthorizedAccessException();
        }
        if(order.getStatus()!=OrderStatus.PENDING_SYNC){
            throw new BusinessException("Request is not pending synchronization");
        }
        try{
            orderMessagingProducer.sendStockUpdate(order.getItems().stream().map(item ->
                    new StockItemDto(item.getProductId(), item.getQuantity())).toList());
            order.setStatus(OrderStatus.PAID);
            return modelMapper.map(orderRepository.save(order), OrderResponseDto.class);
        } catch (KafkaSendException e){
            throw new BusinessException("Falha na sincronização. Tente novamente mais tarde.");
        }
    }
}
