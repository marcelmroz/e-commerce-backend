package com.example.ecommercebackend.services.impl;

import com.example.ecommercebackend.dtos.ProductDto;
import com.example.ecommercebackend.entities.Product;
import com.example.ecommercebackend.exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.mappers.ProductMapper;
import com.example.ecommercebackend.repositories.ProductRepository;
import com.example.ecommercebackend.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map((product)->ProductMapper.mapToProductDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto updatedProduct) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product with given id:" + productId + "does not exist.")
        );

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setColor(updatedProduct.getColor());
        product.setCategory(updatedProduct.getCategory());
        product.setSize(updatedProduct.getSize());
        product.setStockQuantity(product.getStockQuantity());

        Product updatedProjectObj = productRepository.save(product);
        return ProductMapper.mapToProductDto(updatedProjectObj);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product with given id:" + productId + "does not exist.")
        );

        productRepository.deleteById(productId);
    }


}
