package com.ms.productservice.services;


import com.example.sharedfilesmodule.dtos.restaurant.RestaurantCreatedEvent;
import com.ms.productservice.client.RestaurantClient;
import com.ms.productservice.dtos.ProductRequestDto;
import com.ms.productservice.dtos.ProductResponseDTO;
import com.ms.productservice.entities.ProductEntity;
import com.ms.productservice.exceptions.ProductAlreadyExistsException;
import com.ms.productservice.exceptions.ProductNotFoundException;
import com.ms.productservice.exceptions.UnauthorizedAccessException;
import com.ms.productservice.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final RestaurantCacheService restaurantCacheService;
    private final RestaurantClient restaurantClient;
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDto productRequestDto, UUID restaurantId, UUID ownerId){
        if(productRepository.existsByName(productRequestDto.name())){
            throw new ProductAlreadyExistsException();
        }
        validateRestaurantOwnership(restaurantId, ownerId);
        var productEntity = modelMapper.map(productRequestDto, ProductEntity.class);
        productEntity.setRestaurantId(restaurantId);
        var savedProduct = productRepository.save(productEntity);
        return modelMapper.map(savedProduct, ProductResponseDTO.class);
    }
    @Transactional
    public ProductResponseDTO updateProduct(ProductRequestDto productRequestDto, UUID idProduct, UUID restaurantId, UUID ownerId){
        validateRestaurantOwnership(restaurantId, ownerId);
        var product = productRepository.findById(idProduct).orElseThrow(ProductNotFoundException::new);
        modelMapper.map(productRequestDto, product);
        var updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductResponseDTO.class);
    }
    public ProductResponseDTO getProduct(UUID idProduct){
        var product = productRepository.findById(idProduct).orElseThrow(ProductNotFoundException::new);
        return modelMapper.map(product, ProductResponseDTO.class);
    }
    public List<ProductResponseDTO> getAllProducts(){
        return productRepository.findAll().stream().map(product -> modelMapper.map(product, ProductResponseDTO.class))
                .toList();
    }
    @Transactional
    public void deleteProduct(UUID idProduct, UUID restaurantId, UUID ownerId){
        validateRestaurantOwnership(restaurantId, ownerId);
        productRepository.delete(productRepository.findById(idProduct).orElseThrow(ProductNotFoundException::new));
    }
    private void validateRestaurantOwnership(UUID restaurantId, UUID ownerId){
        Boolean isOwnerCache = restaurantCacheService.isRestaurantOwner(restaurantId,ownerId);
        if(Boolean.TRUE.equals(isOwnerCache)){
            return;
        }
        if(Boolean.FALSE.equals(isOwnerCache)){
            throw new UnauthorizedAccessException("You don't have authorization for this content");
        }
        var response = restaurantClient.isRestaurantOwner(restaurantId, ownerId);
        if(Boolean.FALSE.equals(response.getBody())){
            throw new UnauthorizedAccessException("You don't have authorization for this content");
        }
        restaurantCacheService.saveRestaurantMetaData(new RestaurantCreatedEvent(restaurantId, ownerId));
    }
}

