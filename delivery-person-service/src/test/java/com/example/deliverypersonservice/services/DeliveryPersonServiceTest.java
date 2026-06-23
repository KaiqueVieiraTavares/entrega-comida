package com.example.deliverypersonservice.services;

import com.example.deliverypersonservice.dtos.DeliveryPersonResponseDto;
import com.example.deliverypersonservice.dtos.DeliveryPersonUpdateDto;
import com.example.deliverypersonservice.entities.DeliveryPersonEntity;
import com.example.deliverypersonservice.exceptions.DeliveryPersonNotFound;
import com.example.deliverypersonservice.repositories.DeliveryPersonRepository;
import com.example.deliverypersonservice.templates.DeliveryPersonTemplateFactory;
import com.example.sharedfilesmodule.dtos.deliveryperson.DeliveryPersonCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class DeliveryPersonServiceTest {
    @InjectMocks
    private DeliveryPersonService deliveryPersonService;
    @Mock
    private DeliveryPersonRepository deliveryPersonRepository;
    @Mock
    private ModelMapper modelMapper;
    private DeliveryPersonEntity deliveryPerson;
    private DeliveryPersonResponseDto deliveryPersonResponseDto;
    private DeliveryPersonUpdateDto deliveryPersonUpdateDto;
    private DeliveryPersonCreatedEvent deliveryPersonCreatedEvent;

    @BeforeEach
    void setUp() {
        deliveryPerson = DeliveryPersonTemplateFactory.createValidDeliveryPersonEntity();
        deliveryPersonResponseDto = DeliveryPersonTemplateFactory.createValidDeliveryPersonResponseDto();
        deliveryPersonUpdateDto = DeliveryPersonTemplateFactory.createValidDeliveryPersonUpdateDto();
        deliveryPersonCreatedEvent = DeliveryPersonTemplateFactory.createValidDeliveryPersonCreatedEvent();
    }

    @Test
    void createDeliveryPerson() {
        when(modelMapper.map(deliveryPersonCreatedEvent, DeliveryPersonEntity.class)).thenReturn(deliveryPerson);
        when(deliveryPersonRepository.save(deliveryPerson)).thenReturn(deliveryPerson);

        deliveryPersonService.createDeliveryPerson(deliveryPersonCreatedEvent);

        verify(modelMapper,times(1)).map(deliveryPersonCreatedEvent, DeliveryPersonEntity.class);
        verify(deliveryPersonRepository,times(1)).save(deliveryPerson);
        verifyNoMoreInteractions(deliveryPersonRepository, modelMapper);
    }

    @Test
    void getDeliveryPerson() {
        when(deliveryPersonRepository.findById(deliveryPerson.getId())).thenReturn(Optional.of(deliveryPerson));
        when(modelMapper.map(deliveryPerson, DeliveryPersonResponseDto.class)).thenReturn(deliveryPersonResponseDto);

        DeliveryPersonResponseDto result = deliveryPersonService.getDeliveryPerson(deliveryPerson.getId());

        assertEquals(deliveryPersonResponseDto, result);
        verify(deliveryPersonRepository,times(1)).findById(deliveryPerson.getId());
        verify(modelMapper,times(1)).map(deliveryPerson, DeliveryPersonResponseDto.class);
        verifyNoMoreInteractions(deliveryPersonRepository, modelMapper);

    }

    @Test
    void getDeliveryPerson_ShouldThrowAnExceptionWhenDeliveryPersonNotFound(){
        when(deliveryPersonRepository.findById(deliveryPerson.getId())).thenReturn(Optional.empty());

        assertThrows(DeliveryPersonNotFound.class, () -> deliveryPersonService.getDeliveryPerson(deliveryPerson.getId()));

        verify(modelMapper,never()).map(any(),any());
        verifyNoMoreInteractions(deliveryPersonRepository, modelMapper);
    }
    @Test
    void getAllDeliveryPerson() {
        when(deliveryPersonRepository.findAll()).thenReturn(List.of(deliveryPerson));
        when(modelMapper.map(deliveryPerson, DeliveryPersonResponseDto.class)).thenReturn(deliveryPersonResponseDto);

        List<DeliveryPersonResponseDto> results = deliveryPersonService.getAllDeliveryPerson();

        assertEquals(deliveryPersonResponseDto, results.get(0));
        assertEquals(1, results.size());
        verify(deliveryPersonRepository,times(1)).findAll();
        verify(modelMapper,times(1)).map(deliveryPerson, DeliveryPersonResponseDto.class);
        verifyNoMoreInteractions(deliveryPersonRepository, modelMapper);
    }

    @Test
    void updateDeliveryPerson() {
        when(deliveryPersonRepository.findById(deliveryPerson.getId())).thenReturn(Optional.of(deliveryPerson));

        doAnswer(invocation -> {
            DeliveryPersonUpdateDto updateDto = invocation.getArgument(0);
            DeliveryPersonEntity deliveryPersonEntity = invocation.getArgument(1);

            deliveryPersonEntity.setVehicleType(updateDto.vehicleType());
            deliveryPersonEntity.setName(updateDto.name());
            deliveryPersonEntity.setCnh(updateDto.cnh());
            deliveryPersonEntity.setVehiclePlate(updateDto.vehiclePlate());

            return null;
        }).when(modelMapper).map(deliveryPersonUpdateDto, deliveryPerson);

        when(deliveryPersonRepository.save(deliveryPerson)).thenReturn(deliveryPerson);
        when(modelMapper.map(deliveryPerson, DeliveryPersonResponseDto.class)).thenReturn(deliveryPersonResponseDto);

        DeliveryPersonResponseDto result = deliveryPersonService.updateDeliveryPerson(deliveryPerson.getId(), deliveryPersonUpdateDto);

        assertEquals(deliveryPersonResponseDto, result);
        verify(deliveryPersonRepository,times(1)).findById(deliveryPerson.getId());
        verify(modelMapper,times(1)).map(deliveryPersonUpdateDto, deliveryPerson);
        verify(deliveryPersonRepository,times(1)).save(deliveryPerson);
        verify(modelMapper,times(1)).map(deliveryPerson, DeliveryPersonResponseDto.class);
        verifyNoMoreInteractions(deliveryPersonRepository, modelMapper);
    }
    @Test
    void updateDeliveryPerson_ShouldThrowAnExceptionWhenDeliveryPersonNotFound(){
        when(deliveryPersonRepository.findById(deliveryPerson.getId())).thenReturn(Optional.empty());

        assertThrows(DeliveryPersonNotFound.class, () -> deliveryPersonService.updateDeliveryPerson(deliveryPerson.getId(), deliveryPersonUpdateDto));

        verify(modelMapper,never()).map(any(),any());
        verify(deliveryPersonRepository,never()).save(any());
        verifyNoMoreInteractions(deliveryPersonRepository, modelMapper);
    }
    @Test
    void deleteDeliveryPerson() {
        when(deliveryPersonRepository.findById(deliveryPerson.getId())).thenReturn(Optional.of(deliveryPerson));

        deliveryPersonService.deleteDeliveryPerson(deliveryPerson.getId());

        verify(deliveryPersonRepository,times(1)).findById(deliveryPerson.getId());
        verify(deliveryPersonRepository,times(1)).delete(deliveryPerson);
        verifyNoMoreInteractions(deliveryPersonRepository);
    }

    @Test
    void deleteDeliveryPerson_ShouldThrowAnExceptionWhenDeliveryPersonNotFound(){
        when(deliveryPersonRepository.findById(deliveryPerson.getId())).thenReturn(Optional.empty());

        assertThrows(DeliveryPersonNotFound.class,() -> deliveryPersonService.deleteDeliveryPerson(deliveryPerson.getId()));

        verify(deliveryPersonRepository,never()).delete(any());
        verifyNoMoreInteractions(deliveryPersonRepository);
    }
}