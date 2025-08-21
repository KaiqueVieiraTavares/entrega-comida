package com.ms.orderservice.services;

import com.ms.orderservice.dtos.*;
import com.ms.shared.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {
    private final UUID orderId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();
    private final UUID restaurantId = UUID.randomUUID();
    private final UUID productId = UUID.randomUUID();
    private final String productName = "Pizza Calabresa";
    private final Integer quantity = 2;
    private final BigDecimal price = BigDecimal.valueOf(50.00);
    private final BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));
    private final LocalDateTime createdAt = LocalDateTime.now();
    private final OrderStatus status = OrderStatus.PENDING_PAYMENT;
    private CreateOrderResponseDto createOrderResponseDto;
    private OrderItemRequestDto orderItemRequestDto;
    private OrderItemResponseDto orderItemResponseDto;
    private OrderRequestDto orderRequestDto;
    private OrderResponseDto orderResponseDto;
    @BeforeEach
    void setUp() {
        orderItemRequestDto = new OrderItemRequestDto(productId, productName, price, quantity);
        orderItemResponseDto = new OrderItemResponseDto(productId, productName, quantity, price);
        orderRequestDto = new OrderRequestDto(List.of(orderItemRequestDto));
        orderResponseDto = new OrderResponseDto(orderId, status, totalPrice, List.of(orderItemResponseDto));
        createOrderResponseDto = new CreateOrderResponseDto(orderId, "Pedido criado com sucesso!");
    }

    @Test
    void createOrder() {

    }

    @Test
    void getOrder() {
    }

    @Test
    void getAllOrders() {
    }

    @Test
    void deleteOrder() {
    }

    @Test
    void updateOrder() {
    }

    @Test
    void confirmOrder() {
    }
}