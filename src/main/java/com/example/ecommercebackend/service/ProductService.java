package com.example.ecommercebackend.service;

import com.example.ecommercebackend.dto.ProductDto;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    ProductDto getProductById(Long productId);
}
