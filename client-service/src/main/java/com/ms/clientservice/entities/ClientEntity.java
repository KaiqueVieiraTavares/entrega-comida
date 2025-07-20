package com.ms.clientservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "client")
public class ClientEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nome;
    private String email;
    private String phone;
    private String password;
}
