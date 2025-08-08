package com.ms.orderservice.entities;


import com.ms.orderservice.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    @Id
    private UUID id;
    private UUID userId;
    private UUID restaurantId;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private BigDecimal totalPrice;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<OrderItemEntity> items = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }

    public void addItem(OrderItemEntity orderItemEntity){
        items.add(orderItemEntity);
        orderItemEntity.setOrder(this);
    }
    public void removeItem(OrderItemEntity orderItemEntity){
        items.remove(orderItemEntity);
        orderItemEntity.setOrder(null);
    }
}
