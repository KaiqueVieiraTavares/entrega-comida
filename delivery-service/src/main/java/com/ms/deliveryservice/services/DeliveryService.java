package com.ms.deliveryservice.services;


import com.example.sharedfilesmodule.dtos.OrderConfirmedDto;
import com.example.sharedfilesmodule.enums.DeliveryStatus;

import com.ms.deliveryservice.dtos.DeliveryResponseDTO;

import com.ms.deliveryservice.entities.DeliveryEntity;
import com.ms.deliveryservice.exceptions.DeliveryAlreadyCompletedException;


import com.ms.deliveryservice.exceptions.DeliveryNotFoundException;
import com.ms.deliveryservice.messaging.producer.delivery_notification.NotificationMessagingProducer;
import com.ms.deliveryservice.repositories.DeliveryRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final ModelMapper modelMapper;
    private final NotificationMessagingProducer notificationMessagingProducer;
    public DeliveryService(DeliveryRepository deliveryRepository, ModelMapper modelMapper, NotificationMessagingProducer notificationMessagingProducer) {
        this.deliveryRepository = deliveryRepository;
        this.modelMapper = modelMapper;
        this.notificationMessagingProducer = notificationMessagingProducer;
    }

    //internal
    @Transactional
    public void createDelivery(OrderConfirmedDto orderConfirmedDto){
        DeliveryEntity deliveryEntity = modelMapper.map(orderConfirmedDto, DeliveryEntity.class);
        deliveryRepository.save(deliveryEntity);
    }
    @Transactional(readOnly = true)
    public List<DeliveryResponseDTO> getAvailableDeliveries(){
        return deliveryRepository.findByStatus(DeliveryStatus.WAITING_ASSIGNMENT).stream().map(delivery
                -> modelMapper.map(delivery, DeliveryResponseDTO.class)).toList();
    }
    @Transactional(readOnly = true)
    public List<DeliveryResponseDTO> getMeDeliveries(UUID deliveryPersonId){
        return deliveryRepository.findByDeliveryPersonId(deliveryPersonId).stream().map(delivery
                -> modelMapper.map(delivery, DeliveryResponseDTO.class)).toList();
    }

    @Transactional
    public DeliveryResponseDTO assignDelivery(UUID deliveryPersonId, UUID deliveryId ){
        if(deliveryId==null || deliveryPersonId==null){
            throw new IllegalArgumentException("Id's cannot be null");
        }
        var delivery = deliveryRepository.findById(deliveryId).orElseThrow(() ->
                        new DeliveryNotFoundException("Delivery with id: %s and delivery man with id: %s not found".formatted(deliveryId, deliveryPersonId)));
        delivery.setDeliveryPersonId(deliveryPersonId);
        delivery.setStatus(DeliveryStatus.ASSIGNED);
        log.info("Delivery with id: {} changed status to assigned", deliveryId);
        notificationMessagingProducer.sendMessageWhenOrderIsOnRoute(delivery.getUserId());
        return modelMapper.map(deliveryRepository.save(delivery), DeliveryResponseDTO.class);
    }
    @Transactional
    public DeliveryResponseDTO orderArrived(UUID deliveryPersonId, UUID deliveryId){
        var delivery = deliveryRepository.findByDeliveryPersonIdAndId(deliveryPersonId,deliveryId).orElseThrow(() ->
                new DeliveryNotFoundException("Delivery with id: %s and delivery man with id: %s not found".formatted(deliveryId, deliveryPersonId))
        );
        delivery.setStatus(DeliveryStatus.ARRIVED);
        log.info("Delivery with id: {} changed status to arrived", deliveryId);
        var savedDelivery = deliveryRepository.save(delivery);
        notificationMessagingProducer.sendMessageToUserWhenOrderArrived(savedDelivery.getUserId());
        return modelMapper.map(savedDelivery, DeliveryResponseDTO.class);
    }

    @Transactional
    public void cancelDelivery(UUID deliveryPersonId, UUID deliveryId){
        var delivery = deliveryRepository.findByDeliveryPersonIdAndId(deliveryPersonId, deliveryId).orElseThrow(() ->
                new DeliveryNotFoundException("Delivery with id: %s not found".formatted(deliveryId)));
        if(delivery.getStatus() == DeliveryStatus.DELIVERED){
            throw new DeliveryAlreadyCompletedException("Cannot cancel a delivered delivery");
        }

        delivery.setStatus(DeliveryStatus.FAILED);
        log.info("Delivery with id: {} changed status to failed", deliveryId);
        deliveryRepository.save(delivery);
        notificationMessagingProducer.sendMessageToUserWhenOrderIsCanceled(delivery.getUserId());
    }
}
