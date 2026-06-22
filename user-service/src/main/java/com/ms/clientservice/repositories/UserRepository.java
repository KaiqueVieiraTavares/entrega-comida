package com.ms.clientservice.repositories;

import com.ms.clientservice.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

}
