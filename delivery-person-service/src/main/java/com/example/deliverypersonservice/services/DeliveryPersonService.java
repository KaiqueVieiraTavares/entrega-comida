package com.example.deliverypersonservice.services;

import com.example.deliverypersonservice.dtos.DeliveryPersonRequestDTO;
import com.example.deliverypersonservice.dtos.DeliveryPersonResponseDTO;
import com.example.deliverypersonservice.exceptions.DeliveryPersonNotFound;
import com.example.deliverypersonservice.repositories.DeliveryPersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DeliveryPersonService {
    private final ModelMapper modelMapper;
    private final DeliveryPersonRepository deliveryPersonRepository;
    public DeliveryPersonService(ModelMapper modelMapper, DeliveryPersonRepository deliveryPersonRepository) {
        this.modelMapper = modelMapper;
        this.deliveryPersonRepository = deliveryPersonRepository;
    }

    public DeliveryPersonResponseDTO getDeliveryPerson(UUID id){
        var deliveryPerson = deliveryPersonRepository.findById(id).orElseThrow(() -> {
            log.error("Delivery person with id: {} not found!" ,id);
            return new DeliveryPersonNotFound();
        });
        return modelMapper.map(deliveryPerson, DeliveryPersonResponseDTO.class);
    }
    public List<DeliveryPersonResponseDTO> getAllDeliveryPerson(){
        return deliveryPersonRepository.findAll().stream().map(deliveryPersonEntity ->
                modelMapper.map(deliveryPersonEntity, DeliveryPersonResponseDTO.class)).toList();
    }
    public DeliveryPersonResponseDTO updateDeliveryPerson(UUID id, DeliveryPersonRequestDTO deliveryPersonRequestDTO){
       var deliveryPerson = deliveryPersonRepository.findById(id).orElseThrow(() -> {
           log.error("Delivery person with id: {} not found!" ,id);
           return new DeliveryPersonNotFound();
       });
       modelMapper.map(deliveryPersonRequestDTO, deliveryPerson);
      var savedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);
       return modelMapper.map(savedDeliveryPerson, DeliveryPersonResponseDTO.class);
    }

    public void deleteDeliveryPerson(UUID id){
        var deliveryPerson = deliveryPersonRepository.findById(id).orElseThrow(() -> {
            log.error("Delivery person with id: {} not found!" ,id);
            return new DeliveryPersonNotFound();
        });
        deliveryPersonRepository.delete(deliveryPerson);
    }
}
