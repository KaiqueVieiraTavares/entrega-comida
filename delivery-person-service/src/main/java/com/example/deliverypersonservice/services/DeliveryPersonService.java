package com.example.deliverypersonservice.services;

import com.example.deliverypersonservice.entities.DeliveryPersonEntity;
import com.example.deliverypersonservice.exceptions.DeliveryPersonNotFound;
import com.example.deliverypersonservice.repositories.DeliveryPersonRepository;
import com.example.sharedfilesmodule.dtos.deliveryperson.DeliveryPersonRegisterDTO;
import com.example.sharedfilesmodule.dtos.deliveryperson.DeliveryPersonResponseDto;
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


    public DeliveryPersonResponseDto createDeliveryPerson(DeliveryPersonRegisterDTO deliveryPersonRequestDTO){
        var deliveryPersonEntity = modelMapper.map(deliveryPersonRequestDTO, DeliveryPersonEntity.class);
        return modelMapper.map(deliveryPersonRepository.save(deliveryPersonEntity), DeliveryPersonResponseDto.class);
    }


    public DeliveryPersonResponseDto getDeliveryPerson(UUID id){
        var deliveryPerson = deliveryPersonRepository.findById(id).orElseThrow(() -> {
            log.error("Delivery person with id: {} not found!" ,id);
            return new DeliveryPersonNotFound();
        });
        return modelMapper.map(deliveryPerson, DeliveryPersonResponseDto.class);
    }
    public List<DeliveryPersonResponseDto> getAllDeliveryPerson(){
        return deliveryPersonRepository.findAll().stream().map(deliveryPersonEntity ->
                modelMapper.map(deliveryPersonEntity, DeliveryPersonResponseDto.class)).toList();
    }
    public DeliveryPersonResponseDto updateDeliveryPerson(UUID id, DeliveryPersonRegisterDTO deliveryPersonRequestDTO){
       var deliveryPerson = deliveryPersonRepository.findById(id).orElseThrow(() -> {
           log.error("Delivery person with id: {} not found!" ,id);
           return new DeliveryPersonNotFound();
       });
       modelMapper.map(deliveryPersonRequestDTO, deliveryPerson);
      var savedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);
       return modelMapper.map(savedDeliveryPerson, DeliveryPersonResponseDto.class);
    }

    public void deleteDeliveryPerson(UUID id){
        var deliveryPerson = deliveryPersonRepository.findById(id).orElseThrow(() -> {
            log.error("Delivery person with id: {} not found!" ,id);
            return new DeliveryPersonNotFound();
        });
        deliveryPersonRepository.delete(deliveryPerson);
    }
}
