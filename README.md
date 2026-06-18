#  Food Delivery System - Microservices

Um sistema de entrega de comida desenvolvido com **arquitetura de microsserviços** utilizando **Spring Boot** e diversas tecnologias do ecossistema Cloud.  

---

##  Tecnologias utilizadas

- **Java 21**
- **Spring Boot**
  - Spring Web
  - Spring Security
  - validation
  - lombok
  - sping data JPA 
  - JWT
  - OAuth2 Client
- **Spring Cloud**
  - Eureka (Service Discovery)
  - API Gateway
- **Mensageria**
  - Kafka
- **Comunicação em tempo real**
  - WebSocket
- **Banco de Dados**
  - Mysql
- **JUnit & Mockito** → Testes unitários 
- **Docker** 
---

## Arquitetura

```mermaid
---
config:
  layout: elk
---
flowchart TD
    %% Atores
    Cliente([Usuário / Cliente])
    Motoboy([Motoboy / Entregador])
    Google([OAuth2 Google])

    %% Gateway e Infraestrutura Central
    Gateway[API Gateway-Service<br/>Filtro JWT e Roteamento]
    Eureka[(Eureka-Server<br/>Service Discovery)]
    Kafka{{Kafka<br/>Mensageria Event-Driven}}
    DB[(Bancos de Dados<br/>MySQL Separados)]

    %% Agrupamento de Microsserviços
    subgraph Microsserviços [Arquitetura de Microsserviços Internos]
        direction TB
        Auth[Auth-Service]
        User[User-Service]
        Rest[Restaurant-Service]
        Prod[Product-Service]
        Order[Order-Service]
        Delivery[Delivery-Service]
        Notif[Notification-Service]
    end

    %% Fluxo de Entrada e Autenticação
    Cliente -->|Requests REST / Login| Gateway
    Motoboy -->|Requests REST| Gateway
    
    Gateway -->|Roteia p/ Autenticar| Auth
    Auth <-->|Integração| Google
    
    %% Fluxo Principal (Roteamento do Gateway)
    Gateway -->|Roteia requisições validadas| Order
    Gateway -->|Roteia requisições validadas| User
    Gateway -->|Roteia requisições validadas| Rest
    Gateway -->|Roteia requisições validadas| Delivery

    %% Comunicação Assíncrona (Kafka)
    Order -.->|Publica: Chamada Assíncrona / Pedido Criado| Kafka
    Prod -.->|Consome: Chamada / Publica: Resposta| Kafka
    Rest -.->|Publica: Eventos do Restaurante| Kafka
    Delivery -.->|Consome: Pedido / Publica: Status| Kafka
    Notif -.->|Consome: Eventos do Sistema| Kafka

    %% Comunicação em Tempo Real (WebSocket)
    Notif -.->|WebSocket: Envia Notificações| Gateway
    Gateway -.->|WebSocket: Push em Tempo Real| Cliente
    Gateway -.->|WebSocket: Push em Tempo Real| Motoboy

    %% Persistência
    User & Rest & Prod & Order & Delivery --> DB

    %% Service Discovery
    Auth -.->|Registra| Eureka
    User -.->|Registra| Eureka
    Order -.->|Descobre/Registra| Eureka
    Delivery -.->|Registra| Eureka
    Notif -.->|Registra| Eureka
    Prod -.->|Registra| Eureka
    Rest -.->|Registra| Eureka
    Gateway -.->|Descobre rotas| Eureka

    %% Estilização de Cores
    classDef actor fill:#e1f5fe,stroke:#0288d1,stroke-width:2px,color:#000
    classDef gateway fill:#ffe082,stroke:#ff8f00,stroke-width:2px,color:#000
    classDef service fill:#c8e6c9,stroke:#388e3c,stroke-width:2px,color:#000
    classDef infra fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px,color:#000
    classDef db fill:#cfd8dc,stroke:#455a64,stroke-width:2px,color:#000

    class Cliente,Motoboy,Google actor
    class Gateway gateway
    class Auth,User,Rest,Prod,Order,Delivery,Notif service
    class Eureka,Kafka infra
    class DB db
```
O sistema segue uma arquitetura de microsserviços, composta por:

- **Auth-Service** → Responsável por autenticação e autorização (JWT + OAuth2).
- **User-Service** → Gerenciamento de usuários.
- **Product-Service** → Cadastro e atualização de produtos (API interna).
- **Order-Service** → Criação e gerenciamento de pedidos.
- **Delivery-Service** → API dos motoboys, responsável por pegar pedidos, atualizar status e finalizar entregas.
- **Notification-Service** → Responsável por enviar notificações entre microsserviços e para os usuários.
- **Restaurant-Service** → Cadastro e gerenciamento de restaurantes.
- **Shared-Files** → Módulo compartilhado contendo DTOs e Enums comuns.
- **Gateway-Service** → Porta de entrada da aplicação e filtro de validação JWT.
- **Eureka-Server** → Registro e descoberta de microsserviços.


---

##  Autenticação

- Login e registro via **JWT**.
- Integração com **OAuth2 (Google)**.
- Validação centralizada no **API Gateway**.

---

##  Fluxo principal

1. O usuário se autentica no **Auth-Service**.  
2. Os pedidos são criados no **Order-Service**, que consulta o **Product-Service**.  
3. As mensagens entre os serviços são trocadas via **Kafka**.  
4. O acompanhamento do pedido em tempo real é feito por **WebSocket**.  
5. O **Delivery-Service** organiza a entrega.  
