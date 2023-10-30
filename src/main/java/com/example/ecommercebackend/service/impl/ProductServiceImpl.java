package com.example.ecommercebackend.service.impl;

import com.example.ecommercebackend.dto.ProductDto;
import com.example.ecommercebackend.entity.Product;
import com.example.ecommercebackend.exception.ResourceNotFoundException;
import com.example.ecommercebackend.mapper.ProductMapper;
import com.example.ecommercebackend.repository.ProductRepository;
import com.example.ecommercebackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    @Override
    public ProductDto createProduct(ProductDto productDto) {

        Product product = ProductMapper.mapToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.mapToProductDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with given id:" + productId + "does not exist."));
        return ProductMapper.mapToProductDto(product);
    }
}
