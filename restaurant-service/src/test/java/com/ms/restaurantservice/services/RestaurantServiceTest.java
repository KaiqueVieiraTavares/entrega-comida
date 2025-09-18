package com.ms.restaurantservice.services;

import com.ms.restaurantservice.dtos.restaurant.RestaurantCreateDto;
import com.ms.restaurantservice.dtos.restaurant.RestaurantResponseDto;
import com.ms.restaurantservice.dtos.restaurant.RestaurantUpdateDto;
import com.ms.restaurantservice.entities.RestaurantEntity;
import com.ms.restaurantservice.enums.Category;
import com.ms.restaurantservice.exceptions.restaurant.RestaurantAlreadyExistsException;
import com.ms.restaurantservice.exceptions.restaurant.RestaurantNotFoundException;
import com.ms.restaurantservice.exceptions.restaurant.UnauthorizedAccessException;
import com.ms.restaurantservice.repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RestaurantRepository restaurantRepository;
    @InjectMocks
    private RestaurantService restaurantService;
    private final UUID id = UUID.randomUUID();
    private final UUID ownerId = UUID.randomUUID();
    private final String name = "Homemade Flavor Restaurant";
    private final String email = "contact@homemadeflavor.com";
    private final String password = "securePass123";
    private final String cnpj = "98.765.432/0001-55"; // Tax ID in Brazil
    private final String phone = "(11) 98888-7777";
    private final String address = "1500 Paulista Avenue";
    private final String city = "São Paulo";
    private final String state = "SP";
    private final String cep = "01310-200"; // Postal code
    private final String description = "Family-owned restaurant specialized in Brazilian homemade food.";
    private final Category category = Category.RESTAURANT;
    private RestaurantCreateDto restaurantCreateDto;
    private RestaurantResponseDto restaurantResponseDto;
    private RestaurantUpdateDto restaurantUpdateDto;
    private RestaurantEntity restaurantEntity;

    @BeforeEach
    void setup() {
        restaurantEntity = RestaurantEntity.builder()
                .id(id)
                .ownerId(ownerId)
                .name(name)
                .email(email)
                .password(password)
                .cnpj(cnpj)
                .phone(phone)
                .address(address)
                .city(city)
                .state(state)
                .cep(cep)
                .description(description)
                .category(category)
                .build();

        restaurantCreateDto = new RestaurantCreateDto(
                name,
                email,
                password,
                cnpj,
                phone,
                address,
                city,
                state,
                cep,
                description,
                category
        );
        restaurantResponseDto = new RestaurantResponseDto(
                id,
                name,
                email,
                cnpj,
                phone,
                address,
                city,
                state,
                cep,
                description,
                category
        );
        restaurantUpdateDto = new RestaurantUpdateDto(
                name,
                phone,
                address,
                city,
                state,
                cep,
                description,
                category
        );
    }
    @Test
    void createRestaurant() {
        when(restaurantRepository.existsByName(restaurantCreateDto.name())).thenReturn(Boolean.valueOf(false));
        when(modelMapper.map(restaurantCreateDto, RestaurantEntity.class)).thenReturn(restaurantEntity);
        when(restaurantRepository.save(restaurantEntity)).thenReturn(restaurantEntity);
        when(modelMapper.map(restaurantEntity, RestaurantResponseDto.class)).thenReturn(restaurantResponseDto);

        var response = restaurantService.createRestaurant(ownerId,restaurantCreateDto);


        assertEquals(restaurantResponseDto,response);
        verify(restaurantRepository, times(1)).existsByName(restaurantCreateDto.name());
        verify(modelMapper,times(1)).map(restaurantCreateDto, RestaurantEntity.class);
        verify(restaurantRepository,times(1)).save(restaurantEntity);
        verify(modelMapper,times(1)).map(restaurantEntity, RestaurantResponseDto.class);
    }
    @Test
    void createRestaurant_ShouldThrownExceptionWhenNameIsAlreadyUsed(){
        when(restaurantRepository.existsByName(restaurantCreateDto.name())).thenReturn(Boolean.valueOf(true));

        var exception = assertThrows(RestaurantAlreadyExistsException.class,() ->
                restaurantService.createRestaurant(ownerId, restaurantCreateDto));

        assertEquals("Restaurant already exists!", exception.getMessage());
    }
    @Test
    void deleteRestaurant() {
        when(restaurantRepository.findById(any(UUID.class))).thenReturn(Optional.of(restaurantEntity));
        assertDoesNotThrow(() -> restaurantService.deleteRestaurant(ownerId, id));
        verify(restaurantRepository,times(1)).delete(any(RestaurantEntity.class));


    }
    @Test
    void deleteRestaurant_ShouldThrownExceptionWhenRestaurantNotFound(){
        when(restaurantRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        var exception = assertThrows(RestaurantNotFoundException.class, () -> restaurantService.deleteRestaurant(
                ownerId, restaurantEntity.getId()
        ));

        assertEquals("Restaurant not found", exception.getMessage());
        verify(restaurantRepository,never()).delete(any(RestaurantEntity.class));
    }
    @Test
    void getRestaurantById() {
        when(restaurantRepository.findById(any(UUID.class))).thenReturn(Optional.of(restaurantEntity));
        when(modelMapper.map(restaurantEntity, RestaurantResponseDto.class)).thenReturn(restaurantResponseDto);

        var response = restaurantService.getRestaurantById(id);

        assertEquals(restaurantResponseDto, response);
        verify(restaurantRepository,times(1)).findById(any(UUID.class));
        verify(modelMapper,times(1)).map(restaurantEntity,RestaurantResponseDto.class);
    }
    @Test
    void getRestaurant_ShouldThrownExceptionWhenRestaurantNotFound(){
        when(restaurantRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var exception = assertThrows(RestaurantNotFoundException.class, () -> restaurantService.getRestaurantById(id));

        assertEquals("Restaurant not found", exception.getMessage());
        verify(modelMapper,never()).map(any(), any());
    }
    @Test
    void getAllRestaurants() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurantEntity));
        when(modelMapper.map(restaurantEntity, RestaurantResponseDto.class)).thenReturn(restaurantResponseDto);

        var response = restaurantService.getAllRestaurants();

        assertEquals(1, response.size());
        assertSame(restaurantResponseDto, response.getFirst());
        verify(modelMapper).map(restaurantEntity, RestaurantResponseDto.class);
    }

    @Test
    void updateRestaurant() {
        when(restaurantRepository.findById(any(UUID.class))).thenReturn(Optional.of(restaurantEntity));
        doNothing().when(modelMapper).map(any(RestaurantUpdateDto.class), any(RestaurantEntity.class));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);
        when(modelMapper.map(restaurantEntity, RestaurantResponseDto.class)).thenReturn(restaurantResponseDto);

        var response = restaurantService.updateRestaurant(ownerId,id,restaurantUpdateDto);

        assertEquals(restaurantResponseDto, response);
        verify(restaurantRepository,times(1)).findById(any(UUID.class));
        verify(modelMapper).map(eq(restaurantUpdateDto), any(RestaurantEntity.class));
        verify(restaurantRepository,times(1)).save(any(restaurantEntity.getClass()));
        verify(modelMapper,times(1)).map(restaurantEntity, RestaurantResponseDto.class);
    }
    @Test
    void updateRestaurant_ShouldThrownExceptionWhenRestaurantNotFound(){
        when(restaurantRepository.findById(any(UUID.class))).thenReturn(Optional.empty());


        var exception = assertThrows(RestaurantNotFoundException.class, () -> restaurantService.updateRestaurant(ownerId,id,restaurantUpdateDto));
        assertEquals("Restaurant not found", exception.getMessage());
        verify(modelMapper,never()).map(any(), any());
        verify(restaurantRepository,never()).save(any());
    }
    @Test
    void updateRestaurant_ShouldThrownExceptionWhenOwnerIsNotAuthorized(){
        when(restaurantRepository.findById(any(UUID.class))).thenReturn(Optional.of(restaurantEntity));


        //outro id
        var exception = assertThrows(UnauthorizedAccessException.class, () ->
                restaurantService.updateRestaurant(UUID.randomUUID(),id,restaurantUpdateDto));


        assertEquals("You are not authorized to this restaurant", exception.getMessage());
        verify(modelMapper,never()).map(any(), any());
        verify(restaurantRepository,never()).save(any());

    }
}