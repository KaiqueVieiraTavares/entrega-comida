package com.ms.orderservice.controllers;


import com.ms.orderservice.dtos.CreateOrderResponseDto;
import com.ms.orderservice.dtos.OrderResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    public CreateOrderResponseDto createOrder(){

    }
    public ResponseEntity<OrderResponseDto> getOrder(){

    }
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(){

    }
    public ResponseEntity<OrderResponseDto> updateOrder(){

    }
    public ResponseEntity<Void> deleteOrder(){

    }
    public ResponseEntity<OrderResponseDto> confirmOrder(){

    }
}
