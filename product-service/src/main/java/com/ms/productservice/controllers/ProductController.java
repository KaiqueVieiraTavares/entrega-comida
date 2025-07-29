package com.ms.productservice.controllers;


import com.ms.productservice.dtos.ProductRequestDto;
import com.ms.productservice.dtos.ProductResponseDTO;
import com.ms.productservice.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.ResponseEntity.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(){
        return ok(productService.getAllProducts());
    }

    @GetMapping("/{idProduct}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable UUID idProduct){
        return ok(productService.getProduct(idProduct));
    }

    @PutMapping("/{idProduct}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@RequestBody @Valid ProductRequestDto productRequestDto, @PathVariable UUID idProduct){
        return ok(productService.updateProduct(productRequestDto, idProduct));
    }
    @PostMapping("")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductRequestDto productRequestDto){
        return status(HttpStatus.CREATED).body(productService.createProduct(productRequestDto));
    }

    @DeleteMapping("/{idProduct}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID idProduct){
        productService.deleteProduct(idProduct);
        return status(HttpStatus.NO_CONTENT).build();
    }
}
