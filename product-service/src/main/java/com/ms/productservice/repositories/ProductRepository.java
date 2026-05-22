package com.ms.productservice.repositories;

import com.ms.productservice.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    Boolean existsByName(String name);

    @Modifying
    @Query("UPDATE ProductEntity p SET p.quantity = p.quantity - :quantity where p.id = :productId and p.quantity>= :quantity")
    int decreaseStock(UUID productId, Integer quantity);

    @Modifying
    @Query("UPDATE ProductEntity p set p.quantity = p.quantity + :quantity where p.id = :productId")
    int increaseStock(UUID productId, Integer quantity);
}
