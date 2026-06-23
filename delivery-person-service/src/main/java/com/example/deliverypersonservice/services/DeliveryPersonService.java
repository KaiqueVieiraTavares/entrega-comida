package com.example.deliverypersonservice.services;

import com.example.deliverypersonservice.dtos.DeliveryPersonUpdateDto;
import com.example.deliverypersonservice.entities.DeliveryPersonEntity;
import com.example.deliverypersonservice.exceptions.DeliveryPersonNotFound;
import com.example.deliverypersonservice.repositories.DeliveryPersonRepository;
import com.example.sharedfilesmodule.dtos.deliveryperson.DeliveryPersonCreatedEvent;
import com.example.deliverypersonservice.dtos.DeliveryPersonResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryPersonService {
    private final ModelMapper modelMapper;
    private final DeliveryPersonRepository deliveryPersonRepository;


    @Transactional
    public void createDeliveryPerson(DeliveryPersonCreatedEvent deliveryPersonCreatedEvent){
         deliveryPersonRepository.save(modelMapper.map(deliveryPersonCreatedEvent, DeliveryPersonEntity.class));
    }

    @Transactional(readOnly = true)
    public DeliveryPersonResponseDto getDeliveryPerson(UUID id){
        var deliveryPerson = findByIdOrThrow(id);
        return modelMapper.map(deliveryPerson, DeliveryPersonResponseDto.class);
    }
    @Transactional(readOnly = true)
    public List<DeliveryPersonResponseDto> getAllDeliveryPerson(){
        return deliveryPersonRepository.findAll().stream().map(deliveryPersonEntity ->
                modelMapper.map(deliveryPersonEntity, DeliveryPersonResponseDto.class)).toList();
    }
    @Transactional
    public DeliveryPersonResponseDto updateDeliveryPerson(UUID id, DeliveryPersonUpdateDto deliveryPersonUpdateDto){
       var deliveryPerson = findByIdOrThrow(id);
       modelMapper.map(deliveryPersonUpdateDto, deliveryPerson);
      var savedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);
       return modelMapper.map(savedDeliveryPerson, DeliveryPersonResponseDto.class);
    }
    @Transactional
    public void deleteDeliveryPerson(UUID id){
       var deliveryPerson= findByIdOrThrow(id);
        deliveryPersonRepository.delete(deliveryPerson);
    }
    private DeliveryPersonEntity findByIdOrThrow(UUID deliveryPersonId){
        return deliveryPersonRepository.findById(deliveryPersonId).orElseThrow(() -> {
            log.error("Delivery person with id: {} not found!" ,deliveryPersonId);
            return new DeliveryPersonNotFound();
        });
    }
}
