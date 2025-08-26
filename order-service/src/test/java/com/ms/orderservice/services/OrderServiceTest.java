package com.ms.orderservice.services;

import com.ms.orderservice.dtos.*;
import com.ms.orderservice.entities.OrderEntity;
import com.ms.orderservice.entities.OrderItemEntity;
import com.ms.orderservice.exceptions.OrderNotFoundException;
import com.ms.orderservice.exceptions.UnauthorizedAccessException;
import com.ms.orderservice.messaging.producer.OrderMessagingProducer;
import com.ms.orderservice.repositories.OrderRepository;
import com.ms.shared.dtos.stock.StockItemDto;
import com.ms.shared.dtos.stock.StockValidationRequestDto;
import com.ms.shared.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMessagingProducer orderMessagingProducer;
    @InjectMocks
    private OrderService orderService;
    private final long itemId = 1L;
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
    private OrderEntity orderEntity;
    private OrderItemEntity orderItemEntity;
    private OrderRequestDto emptyOrderRequestDto;
    @BeforeEach
    void setUp() {
        orderItemRequestDto = new OrderItemRequestDto(productId, productName, price, quantity);
        orderItemResponseDto = new OrderItemResponseDto(productId, productName, quantity, price);
        orderRequestDto = new OrderRequestDto(List.of(orderItemRequestDto));
        orderResponseDto = new OrderResponseDto(orderId, status, totalPrice, List.of(orderItemResponseDto));
        createOrderResponseDto = new CreateOrderResponseDto(orderId, "Pedido criado com sucesso!");
        orderItemEntity = new OrderItemEntity(itemId,productId,productName,quantity,price,orderEntity);
        orderEntity = new OrderEntity(orderId,userId,restaurantId,createdAt,status,totalPrice,new ArrayList<>(List.of(orderItemEntity)));
        emptyOrderRequestDto = new OrderRequestDto(List.of());
    }

    @Test
    void createOrder() {
        var response = orderService.createOrder(userId,orderRequestDto);
        assertNotNull(response.orderId());
        assertEquals("Request received, awaiting validation", response.message());
        verify(orderMessagingProducer,times(1)).sendStockValidationRequest(any(StockValidationRequestDto.class));
    }

    @Test
    void getOrder() {
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(orderEntity));
        when(modelMapper.map(orderEntity, OrderResponseDto.class)).thenReturn(orderResponseDto);

        var response = orderService.getOrder(userId,orderId);

        assertEquals(orderResponseDto, response);
        verify(orderRepository,times(1)).findById(any(UUID.class));
        verify(modelMapper,times(1)).map(orderEntity, OrderResponseDto.class);
    }
    @Test
    void getOrder_ShouldThrownAnExceptionWhenOrderNotFound(){
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var exception = assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(userId, orderId));

        assertEquals("Order not found", exception.getMessage());
        verify(modelMapper,never()).map(any(), any());
    }

    @Test
    void getOrder_ShouldThrownAnExceptionWhenUserIsNotAuthorized(){
        UUID otherId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));

        var exception = assertThrows(UnauthorizedAccessException.class, () -> orderService.getOrder(otherId, orderId));
        verifyNoInteractions(modelMapper);
        verify(orderRepository,times(1)).findById(orderId);
    }
    @Test
    void getAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(orderEntity));
        when(modelMapper.map(orderEntity, OrderResponseDto.class)).thenReturn(orderResponseDto);

        var response = orderService.getAllOrders();

        assertEquals(1, response.size());
        assertEquals(orderResponseDto, response.get(0));
        verify(orderRepository,times(1)).findAll();
        verify(modelMapper,times(1)).map(orderEntity, OrderResponseDto.class);
    }

    @Test
    void deleteOrder() {
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(orderEntity));

        orderService.deleteOrder(userId, orderId);
        verify(orderRepository,times(1)).delete(orderEntity);
    }
    @Test
    void deleteOrder_ShouldThrownAnExceptionWhenOrderNotFound(){
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var exception = assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(userId, orderId));

        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository,never()).delete(any());

    }
    @Test
    void deleteOrder_ShouldThrownAnExceptionWhenUserIsNotAuthorized(){
        UUID invalidId= UUID.randomUUID();
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(orderEntity));

        var exception = assertThrows(UnauthorizedAccessException.class, () -> orderService.deleteOrder(invalidId, orderId));

        assertEquals("You do not have access to this content", exception.getMessage());
        verify(orderRepository,never()).save(any());
    }
    @Test
    void updateOrder() {
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(modelMapper.map(orderEntity,OrderResponseDto.class)).thenReturn(orderResponseDto);

        var response = orderService.updateOrder(userId, orderId, orderRequestDto);

        assertEquals(orderResponseDto, response);
        verify(orderRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository,times(1)).save(orderEntity);
        verify(modelMapper,times(1)).map(orderEntity, OrderResponseDto.class);
    }
    @Test
    void updateOrder_ShouldThrownAnExceptionWhenUserIsNotAuthorized(){
        UUID invalidUUId = UUID.randomUUID();
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(orderEntity));

        var exception = assertThrows(UnauthorizedAccessException.class,() -> orderService.updateOrder(invalidUUId, orderId, orderRequestDto));

        assertEquals("You do not have access to this content", exception.getMessage());
        verify(orderRepository,never()).save(any());
        verify(modelMapper,never()).map(any(),any());
    }

    @Test
    void updateOrder_ShouldThrownAnExceptionWhenRequestIsEmpty(){
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(orderEntity));

        var exception = assertThrows(IllegalArgumentException.class, () -> orderService.updateOrder(userId, orderId, emptyOrderRequestDto));

        verify(orderRepository,never()).save(any());
        verify(modelMapper,never()).map(any(),any());
    }

    @Test
    void confirmOrder() {
        List<StockItemDto> items = List.of(new StockItemDto(productId, quantity));
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(modelMapper.map(orderEntity,OrderResponseDto.class)).thenReturn(orderResponseDto);

        var response = orderService.confirmOrder(userId, orderId);

        assertEquals(orderResponseDto, response);
        assertEquals(OrderStatus.PAID, orderEntity.getStatus());
        verify(orderRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository,times(1)).save(orderEntity);
        verify(modelMapper,times(1)).map(orderEntity, OrderResponseDto.class);
        verify(orderMessagingProducer,times(1)).sendStockUpdate(items);

    }

    @Test
    void confirmOrder_ShouldThrownAnExceptionWhenUserIsNotAuthorized(){
        UUID invalidUUId = UUID.randomUUID();
        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(orderEntity));

        var exception = assertThrows(UnauthorizedAccessException.class,() -> orderService.confirmOrder(invalidUUId, orderId));
        assertEquals("You do not have access to this content", exception.getMessage());
        verify(orderRepository,never()).save(any());
        verify(modelMapper,never()).map(any(),any());
        verify(orderMessagingProducer,never()).sendStockUpdate(any());
    }
}