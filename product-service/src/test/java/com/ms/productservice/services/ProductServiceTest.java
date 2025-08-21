package com.ms.productservice.services;

import com.ms.productservice.dtos.ProductRequestDto;
import com.ms.productservice.dtos.ProductResponseDTO;
import com.ms.productservice.dtos.ProductStockDTO;
import com.ms.productservice.entities.ProductEntity;
import com.ms.productservice.enums.Category;
import com.ms.productservice.exceptions.ProductAlreadyExistsException;
import com.ms.productservice.exceptions.ProductNotFoundException;
import com.ms.productservice.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;
    @Mock
    private ModelMapper modelMapper;
    private final UUID productId = UUID.randomUUID();
    private final UUID restaurantId = UUID.randomUUID();
    private final Long productIdLong = 1L;
    private final String name = "Pizza Calabresa";
    private final String description = "Pizza com calabresa artesanal e queijo derretido";
    private final BigDecimal price = BigDecimal.valueOf(45.90);
    private final Boolean available = true;
    private final String imageUrl = "https://meusite.com/pizza-calabresa.png";
    private final Category category = Category.FOOD;
    private final Integer quantity = 10;

    private ProductRequestDto productRequestDto;
    private ProductResponseDTO productResponseDTO;
    private ProductStockDTO productStockDTO;
    private ProductEntity productEntity;
    @BeforeEach
    void setUp() {
        productEntity = new ProductEntity(productId,restaurantId,name,description,price,imageUrl,category,quantity);

        productRequestDto = new ProductRequestDto(
                name,
                description,
                price,
                available,
                imageUrl,
                category
        );

        productResponseDTO = new ProductResponseDTO(
                productIdLong,
                name,
                description,
                price,
                available,
                imageUrl,
                category
        );

        productStockDTO = new ProductStockDTO(
                productIdLong,
                quantity
        );
    }
    @Test
    void createProduct() {
        when(productRepository.existsByName(any(String.class))).thenReturn(false);
        when(modelMapper.map(productRequestDto, ProductEntity.class)).thenReturn(productEntity);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        when(modelMapper.map(productEntity, ProductResponseDTO.class)).thenReturn(productResponseDTO);


        var response = productService.createProduct(productRequestDto);


        assertEquals(productResponseDTO, response);
        verify(productRepository,times(1)).existsByName(any(String.class));
        verify(modelMapper,times(1)).map(productRequestDto, ProductEntity.class);
        verify(productRepository,times(1)).save(any(ProductEntity.class));
        verify(modelMapper,times(1)).map(productEntity, ProductResponseDTO.class);
    }
    @Test
    void createProduct_WhenNameIsAlreadyUsedThrownException(){
        when(productRepository.existsByName(any(String.class))).thenReturn(true);

        var exception = assertThrows(ProductAlreadyExistsException.class, () ->
                productService.createProduct(productRequestDto));

        assertEquals("The name of the product already exists", exception.getMessage());
        verify(modelMapper,never()).map(any(), any());
        verify(productRepository,never()).save(any());
    }
    @Test
    void updateProduct() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(productEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        when(modelMapper.map(productEntity, ProductResponseDTO.class)).thenReturn(productResponseDTO);

        var response = productService.updateProduct(productRequestDto,productId);

        assertEquals(productResponseDTO,response);
        verify(productRepository,times(1)).findById(any(UUID.class));
        verify(modelMapper).map(productRequestDto, productEntity);
        verify(productRepository,times(1)).save(any(ProductEntity.class));
        verify(modelMapper,times(1)).map(productEntity, ProductResponseDTO.class);
    }
    @Test
    void updateProduct_WhenProductNotFoundThrownException(){
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.empty());


        var exception = assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(productRequestDto, productId));

        assertEquals("Product not found", exception.getMessage());
        verifyNoMoreInteractions(productRepository, modelMapper);
    }
    @Test
    void getProduct() {
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(productEntity));
        when(modelMapper.map(productEntity, ProductResponseDTO.class)).thenReturn(productResponseDTO);

        var response = productService.getProduct(productId);

        assertEquals(productResponseDTO, response);
        verify(productRepository,times(1)).findById(any(UUID.class));
        verify(modelMapper,times(1)).map(productEntity, ProductResponseDTO.class);
    }
    @Test
    void getProduct_WhenProductNotFoundThrownException(){
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var exception = assertThrows(ProductNotFoundException.class, () -> productService.getProduct(productId));


        assertEquals("Product not found", exception.getMessage());
        verifyNoMoreInteractions(modelMapper);
    }
    @Test
    void getAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(productEntity));
        when(modelMapper.map(productEntity, ProductResponseDTO.class)).thenReturn(productResponseDTO);

        var response = productService.getAllProducts();

        assertEquals(productResponseDTO, response.get(0));
        assertEquals(1, response.size());
        verify(productRepository,times(1)).findAll();
        verify(modelMapper,times(1)).map(productEntity, ProductResponseDTO.class);
    }

    @Test
    void deleteProduct() {

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        doNothing().when(productRepository).delete(productEntity);

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(productEntity);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void deleteProduct_WhenProductNotFoundThrownException(){
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        var exception = assertThrows(ProductNotFoundException.class, () ->
                productService.deleteProduct(productId));

        assertEquals("Product not found", exception.getMessage());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).delete(any());
        verifyNoMoreInteractions(productRepository);
    }
}