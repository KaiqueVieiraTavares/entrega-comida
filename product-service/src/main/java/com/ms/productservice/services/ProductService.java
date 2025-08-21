package com.ms.productservice.services;


import com.ms.productservice.dtos.ProductRequestDto;
import com.ms.productservice.dtos.ProductResponseDTO;
import com.ms.productservice.entities.ProductEntity;
import com.ms.productservice.exceptions.ProductAlreadyExistsException;
import com.ms.productservice.exceptions.ProductNotFoundException;
import com.ms.productservice.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDto productRequestDto){
        if(productRepository.existsByName(productRequestDto.name())){
            throw new ProductAlreadyExistsException();
        }
        var savedProduct = productRepository.save(modelMapper.map(productRequestDto, ProductEntity.class));
        return modelMapper.map(savedProduct, ProductResponseDTO.class);
    }
    @Transactional
    public ProductResponseDTO updateProduct(ProductRequestDto productRequestDto, UUID idProduct){
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
    public void deleteProduct(UUID idProduct){
        productRepository.delete(productRepository.findById(idProduct).orElseThrow(ProductNotFoundException::new));
    }
}

