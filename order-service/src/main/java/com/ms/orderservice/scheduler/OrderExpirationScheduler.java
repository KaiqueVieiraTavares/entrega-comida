package com.ms.orderservice.scheduler;

import com.example.sharedfilesmodule.dtos.StockItemDto;
import com.example.sharedfilesmodule.dtos.StockUpdateMessage;
import com.example.sharedfilesmodule.enums.OrderStatus;
import com.ms.orderservice.entities.OrderEntity;
import com.ms.orderservice.messaging.producer.product.ProductRestockProducer;
import com.ms.orderservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderExpirationScheduler {
    private final ProductRestockProducer restockProducer;
    private final OrderRepository orderRepository;



    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cancelExpiredOrders(){
        List<OrderEntity> expiredOrders = orderRepository.findByStatusAndExpiresAtBefore(OrderStatus.PENDING_PAYMENT, LocalDateTime.now());
        for(var order: expiredOrders){
            List<StockItemDto> itemDtos = new ArrayList<>();
            for(var item : order.getItems()){
                var stockItem = new StockItemDto(item.getProductId(), item.getQuantity());
                itemDtos.add(stockItem);
            }
            if(itemDtos.isEmpty()){
                continue;
            }
            restockProducer.sendStockRestore(new StockUpdateMessage(itemDtos));
            order.setStatus(OrderStatus.CANCELED);
        }
    }
}
