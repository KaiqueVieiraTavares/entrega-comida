package com.ms.clientservice.repositories;

import com.ms.clientservice.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {

    Optional<ClientEntity> findByEmail(String email);

    Boolean existsByEmail(String email);
}
