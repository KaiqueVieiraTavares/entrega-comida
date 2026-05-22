package com.ms.orderservice.services;

import com.example.sharedfilesmodule.dtos.StockValidationResponseDto;
import com.example.sharedfilesmodule.enums.OrderStatus;
import com.example.sharedfilesmodule.enums.StockValidationStatus;
import com.ms.orderservice.exceptions.OrderNotFoundException;
import com.ms.orderservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceInternal {
    private final OrderRepository orderRepository;

    @Transactional
    public void processStockValidation(StockValidationResponseDto stockValidationResponseDto){
        var order = orderRepository.findById(stockValidationResponseDto.orderId()).orElseThrow(OrderNotFoundException::new);
        if(stockValidationResponseDto.status() == StockValidationStatus.VALID){
            order.setTotalPrice(stockValidationResponseDto.totalPrice());
            order.setStatus(OrderStatus.PENDING_PAYMENT);
            order.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        } else{
            order.setStatus(OrderStatus.CANCELED);
        }
    }
}
