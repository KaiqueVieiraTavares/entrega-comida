package com.ms.deliveryservice.services;


import com.ms.deliveryservice.dtos.DeliveryRequestDTO;
import com.ms.deliveryservice.dtos.DeliveryResponseDTO;
import com.ms.deliveryservice.dtos.UpdateDeliveryStatusDTO;
import com.ms.deliveryservice.entities.DeliveryEntity;
import com.ms.deliveryservice.exceptions.DeliveryAlreadyCompletedException;
import com.ms.shared.enums.DeliveryStatus;

import com.ms.deliveryservice.exceptions.DeliveryNotFoundException;
import com.ms.deliveryservice.repositories.DeliveryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final ModelMapper modelMapper;
    public DeliveryService(DeliveryRepository deliveryRepository, ModelMapper modelMapper) {
        this.deliveryRepository = deliveryRepository;
        this.modelMapper = modelMapper;
    }

    public void createDelivery(DeliveryRequestDTO deliveryRequestDTO){
        DeliveryEntity deliveryEntity = modelMapper.map(deliveryRequestDTO, DeliveryEntity.class);
        deliveryRepository.save(deliveryEntity);
    }

    public List<DeliveryResponseDTO> getAvailableDeliveries(){
        return deliveryRepository.findByStatus(DeliveryStatus.WAITING_ASSIGNMENT).stream().map(delivery
                -> modelMapper.map(delivery, DeliveryResponseDTO.class)).toList();
    }

    public List<DeliveryResponseDTO> getMeDeliveries(UUID deliveryPersonId){
        return deliveryRepository.findByDeliveryPersonId(deliveryPersonId).stream().map(delivery
                -> modelMapper.map(delivery, DeliveryResponseDTO.class)).toList();
    }


    public DeliveryResponseDTO assignDelivery(UUID deliveryPersonId, UUID deliveryId ){
        var delivery = deliveryRepository.findById(deliveryId).orElseThrow(DeliveryNotFoundException::new);
        delivery.setDeliveryPersonId(deliveryPersonId);
        delivery.setStatus(DeliveryStatus.ASSIGNED);
        return modelMapper.map(deliveryRepository.save(delivery), DeliveryResponseDTO.class);
    }

    public DeliveryResponseDTO updateDelivery(UUID deliveryId, UpdateDeliveryStatusDTO updateDeliveryStatusDTO){
        var delivery = deliveryRepository.findById(deliveryId).orElseThrow(DeliveryNotFoundException::new);
        delivery.setStatus(updateDeliveryStatusDTO.status());
        return modelMapper.map(deliveryRepository.save(delivery), DeliveryResponseDTO.class);
    }
    public void cancelDelivery(UUID deliveryId){
        var delivery = deliveryRepository.findById(deliveryId).orElseThrow(DeliveryNotFoundException::new);
        if(delivery.getStatus() == DeliveryStatus.DELIVERED){
            throw new DeliveryAlreadyCompletedException("Cannot cancel a delivered delivery");
        }
        delivery.setStatus(DeliveryStatus.FAILED);
        deliveryRepository.save(delivery);
    }
}
