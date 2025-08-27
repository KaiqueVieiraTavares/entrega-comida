package com.ms.deliveryservice.services;

import com.ms.deliveryservice.dtos.DeliveryRequestDTO;
import com.ms.deliveryservice.dtos.DeliveryResponseDTO;
import com.ms.deliveryservice.dtos.UpdateDeliveryStatusDTO;
import com.ms.deliveryservice.entities.DeliveryEntity;
import com.ms.deliveryservice.exceptions.DeliveryAlreadyCompletedException;
import com.ms.deliveryservice.exceptions.DeliveryNotFoundException;
import com.ms.deliveryservice.repositories.DeliveryRepository;
import com.ms.shared.enums.DeliveryStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {
    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private DeliveryService deliveryService;

    private final UUID deliveryId = UUID.randomUUID();
    private final UUID orderId = UUID.randomUUID();
    private final UUID restaurantId = UUID.randomUUID();
    private final UUID deliveryPersonId = UUID.randomUUID();
    private final String deliveryAddress = "Rua das Flores, 123";
    private final LocalDateTime createdAt = LocalDateTime.now();
    private final LocalDateTime updatedAt = LocalDateTime.now();
    private final DeliveryStatus status = DeliveryStatus.WAITING_ASSIGNMENT;

    private DeliveryRequestDTO deliveryRequestDTO;
    private DeliveryResponseDTO deliveryResponseDTO;
    private DeliveryEntity deliveryEntity;
    private UpdateDeliveryStatusDTO updateDeliveryStatusDTO;

    @BeforeEach
    void setUp() {
        deliveryRequestDTO = new DeliveryRequestDTO(orderId, restaurantId, deliveryAddress);
        deliveryEntity = new DeliveryEntity(
                deliveryId,
                orderId,
                restaurantId,
                deliveryPersonId,
                deliveryAddress,
                status,
                createdAt,
                updatedAt
        );
        deliveryResponseDTO = new DeliveryResponseDTO(
                deliveryId,
                orderId,
                restaurantId,
                deliveryPersonId,
                deliveryAddress,
                status,
                createdAt,
                updatedAt
        );
        updateDeliveryStatusDTO = new UpdateDeliveryStatusDTO(DeliveryStatus.IN_TRANSIT);
    }


    @Test
    void createDelivery() {
        when(modelMapper.map(deliveryRequestDTO, DeliveryEntity.class)).thenReturn(deliveryEntity);
        when(deliveryRepository.save(deliveryEntity)).thenReturn(deliveryEntity);

        deliveryService.createDelivery(deliveryRequestDTO);

        verify(modelMapper,times(1)).map(deliveryRequestDTO, DeliveryEntity.class);
        verify(deliveryRepository,times(1)).save(deliveryEntity);
    }

    @Test
    void getAvailableDeliveries() {
        when(deliveryRepository.findByStatus(DeliveryStatus.WAITING_ASSIGNMENT)).thenReturn(List.of(deliveryEntity));
        when(modelMapper.map(deliveryEntity, DeliveryResponseDTO.class)).thenReturn(deliveryResponseDTO);


        var response = deliveryService.getAvailableDeliveries();


        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(deliveryResponseDTO,response.get(0));
        verify(deliveryRepository,times(1)).findByStatus(DeliveryStatus.WAITING_ASSIGNMENT);
        verify(modelMapper,times(1)).map(deliveryEntity, DeliveryResponseDTO.class);
    }

    @Test
    void getMeDeliveries() {
        when(deliveryRepository.findByDeliveryPersonId(any(UUID.class))).thenReturn(List.of(deliveryEntity));
        when(modelMapper.map(deliveryEntity, DeliveryResponseDTO.class)).thenReturn(deliveryResponseDTO);

        var response = deliveryService.getMeDeliveries(deliveryPersonId);


        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(deliveryResponseDTO, response.get(0));
        verify(deliveryRepository,times(1)).findByDeliveryPersonId(any(UUID.class));
        verify(modelMapper,times(1)).map(deliveryEntity, DeliveryResponseDTO.class);
    }

    @Test
    void assignDelivery() {
        when(deliveryRepository.findById(any(UUID.class))).thenReturn(Optional.of(deliveryEntity));
        when(deliveryRepository.save(deliveryEntity)).thenReturn(deliveryEntity);

        when(modelMapper.map(any(DeliveryEntity.class), eq(DeliveryResponseDTO.class)))
                .thenAnswer(invocation -> {
                    DeliveryEntity entity = invocation.getArgument(0);
                    return new DeliveryResponseDTO(
                            entity.getId(),
                            entity.getOrderId(),
                            entity.getRestaurantId(),
                            entity.getDeliveryPersonId(),
                            entity.getDeliveryAddress(),
                            entity.getStatus(),
                            entity.getCreatedAt(),
                            entity.getUpdatedAt()
                    );
                });

        var response = deliveryService.assignDelivery(deliveryPersonId, deliveryId);


        assertNotNull(response);
        assertEquals(deliveryPersonId, response.deliveryPersonId());
        assertEquals(DeliveryStatus.ASSIGNED, response.status());

        verify(deliveryRepository, times(1)).findById(any(UUID.class));
        verify(deliveryRepository, times(1)).save(deliveryEntity);
        verify(modelMapper, times(1)).map(any(DeliveryEntity.class), eq(DeliveryResponseDTO.class));
    }

    @Test
    void assignDelivery_ShouldThrownAnExceptionWhenDeliveryNotFound(){
        when(deliveryRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var exception = assertThrows(DeliveryNotFoundException.class, () -> deliveryService.assignDelivery(deliveryPersonId, deliveryId));


        assertEquals("Delivery not found", exception.getMessage());
        verify(modelMapper,never()).map(any(), any());
        verify(deliveryRepository, never()).save(any());
    }
    @Test
    void updateDelivery() {
        when(deliveryRepository.findById(any(UUID.class))).thenReturn(Optional.of(deliveryEntity));
        when(deliveryRepository.save(deliveryEntity)).thenReturn(deliveryEntity);
        when(modelMapper.map(any(DeliveryEntity.class), eq(DeliveryResponseDTO.class)))
                .thenAnswer(invocation -> {
                    DeliveryEntity entity = invocation.getArgument(0);
                    return new DeliveryResponseDTO(
                            entity.getId(),
                            entity.getOrderId(),
                            entity.getRestaurantId(),
                            entity.getDeliveryPersonId(),
                            entity.getDeliveryAddress(),
                            entity.getStatus(),
                            entity.getCreatedAt(),
                            entity.getUpdatedAt()
                    );
                });

        var response = deliveryService.updateDelivery(deliveryId, updateDeliveryStatusDTO);

        assertNotNull(response);
        assertEquals(DeliveryStatus.IN_TRANSIT, response.status());
        verify(deliveryRepository,times(1)).findById(any(UUID.class));
        verify(deliveryRepository,times(1)).save(deliveryEntity);
        verify(modelMapper,times(1)).map(deliveryEntity, DeliveryResponseDTO.class);
    }

    @Test
    void updateDelivery_shouldThrownAnExceptionWhenDeliveryNotFound(){
        when(deliveryRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var exception = assertThrows(DeliveryNotFoundException.class, () -> deliveryService.updateDelivery(deliveryId, updateDeliveryStatusDTO));

        assertEquals("Delivery not found", exception.getMessage());
        verify(deliveryRepository,never()).save(any());
        verify(modelMapper,never()).map(any(), any());
    }
    @Test
    void cancelDelivery() {
        when(deliveryRepository.findById(any(UUID.class))).thenReturn(Optional.of(deliveryEntity));

        deliveryService.cancelDelivery(deliveryId);

        assertEquals(DeliveryStatus.FAILED, deliveryEntity.getStatus());
    }

    @Test
    void cancelDelivery_shouldThrownAnExceptionWhenDeliveryNotFound(){
        when(deliveryRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var exception = assertThrows(DeliveryNotFoundException.class, () -> deliveryService.cancelDelivery(deliveryId));
        assertEquals("Delivery not found", exception.getMessage());
    }
    @Test
    void cancelDelivery_shouldThrownAnDeliveryAlreadyCompletedException(){
        deliveryEntity.setStatus(DeliveryStatus.DELIVERED);
        when(deliveryRepository.findById(any(UUID.class))).thenReturn(Optional.of(deliveryEntity));
        var exception = assertThrows(DeliveryAlreadyCompletedException.class, () -> deliveryService.cancelDelivery(deliveryId));

        assertEquals("Cannot cancel a delivered delivery", exception.getMessage());
    }
}