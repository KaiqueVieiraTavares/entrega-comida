package com.ms.authenticationservice.repositories;

import com.ms.authenticationservice.entities.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, UUID> {
    Boolean existsByEmail(String email);

    Optional<UserAuth> findByEmail(String email);
}
