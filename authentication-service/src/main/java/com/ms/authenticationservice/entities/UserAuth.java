package com.ms.authenticationservice.entities;
import com.example.sharedfilesmodule.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "user_auth")
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private Instant createdAt;
    private Instant updatedAt;

    public static UserAuth create(String email, String passwordHash, Role role){
        return UserAuth.builder().email(email).passwordHash(passwordHash).role(role).build();
    }
    @PrePersist
    private void prePersist(){
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
    @PreUpdate
    private void preUpdate(){
        this.updatedAt = Instant.now();
    }
}
