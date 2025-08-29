package com.ms.orderservice.controllers;


import com.ms.orderservice.dtos.CreateOrderResponseDto;
import com.ms.orderservice.dtos.OrderRequestDto;
import com.ms.orderservice.dtos.OrderResponseDto;
import com.ms.orderservice.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    public ResponseEntity<CreateOrderResponseDto> createOrder(@RequestHeader("X-User-Id") String userId,
                                                              @RequestBody OrderRequestDto orderRequestDto){
        var response  = orderService.createOrder(UUID.fromString(userId), orderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@RequestHeader("X-User-Id") String userId,
                                                     @PathVariable UUID orderId){
        var response = orderService.getOrder(UUID.fromString(userId), orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(){
        var response = orderService.getAllOrders();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(@RequestHeader("X-User-Id") String userId,
                                                        @PathVariable UUID orderId,
                                                        @RequestBody OrderRequestDto orderRequestDto){
        var responseUpdated = orderService.updateOrder(UUID.fromString(userId), orderId, orderRequestDto);
        return ResponseEntity.ok(responseUpdated);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@RequestHeader("X-User-Id")String userId,
                                            @PathVariable UUID orderId){
        orderService.deleteOrder(UUID.fromString(userId), orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PostMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> confirmOrder(@RequestHeader("X-User-Id") String userId,
                                                         @PathVariable UUID orderId){
        var response = orderService.confirmOrder(UUID.fromString(userId), orderId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/retry/{orderId}")
    public ResponseEntity<OrderResponseDto> retryOrder(@RequestHeader("X-User-Id") String userId, @PathVariable UUID orderId){
        var responseRetry = orderService.retryOrderSync(UUID.fromString(userId), orderId);
        return ResponseEntity.ok(responseRetry);
    }
}
