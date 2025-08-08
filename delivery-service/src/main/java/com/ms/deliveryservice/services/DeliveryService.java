package com.ms.deliveryservice.services;


import com.ms.deliveryservice.dtos.DeliveryRequestDTO;
import com.ms.deliveryservice.dtos.DeliveryResponseDTO;
import com.ms.deliveryservice.dtos.UpdateDeliveryStatusDTO;
import com.ms.deliveryservice.entities.DeliveryEntity;
import com.ms.deliveryservice.enums.DeliveryStatus;
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

    public DeliveryResponseDTO createDelivery(DeliveryRequestDTO deliveryRequestDTO){
        DeliveryEntity deliveryEntity = modelMapper.map(deliveryRequestDTO, DeliveryEntity.class);
        return modelMapper.map(deliveryRepository.save(deliveryEntity), DeliveryResponseDTO.class);
    }
    public List<DeliveryResponseDTO> getAvailableDeliveries(){
        return deliveryRepository.findByStatus(DeliveryStatus.PENDING).stream().map(delivery
                -> modelMapper.map(delivery, DeliveryResponseDTO.class)).toList();
    }
    public List<DeliveryResponseDTO> getMeDeliveies(UUID deliveryPersonId){
        return deliveryRepository.findByDeliveryPersonId(deliveryPersonId).stream().map(delivery
                -> modelMapper.map(delivery, DeliveryResponseDTO.class)).toList();
    }
    public DeliveryResponseDTO assignDelivery(UUID deliveryId, UUID deliveryPersonId){
        var delivery = deliveryRepository.findById(deliveryId).orElseThrow(() ->
                new DeliveryNotFoundException("Delivery not found"));
        delivery.setDeliveryPersonId(deliveryPersonId);
        delivery.setStatus(DeliveryStatus.ASSIGNED);
        return modelMapper.map(deliveryRepository.save(delivery), DeliveryResponseDTO.class);
    }
    public DeliveryResponseDTO updateDelivery(UUID deliveryId, UpdateDeliveryStatusDTO updateDeliveryStatusDTO){
        var delivery = deliveryRepository.findById(deliveryId).orElseThrow(() ->
                new DeliveryNotFoundException("Delivery not found"));
        delivery.setStatus(updateDeliveryStatusDTO.status());
        return modelMapper.map(deliveryRepository.save(delivery), DeliveryResponseDTO.class);
    }
}
