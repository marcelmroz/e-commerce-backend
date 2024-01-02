package com.example.ecommercebackend.mappers;

import com.example.ecommercebackend.dtos.ProductDto;
import com.example.ecommercebackend.entities.Product;

public class ProductMapper {
    public static ProductDto mapToProductDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getColor(),
                product.getCategory(),
                product.getSize(),
                product.getStockQuantity()
        );
    }

    public static Product mapToProduct(ProductDto productDto){
        return new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getColor(),
                productDto.getCategory(),
                productDto.getSize(),
                productDto.getStockQuantity()
        );
    }
}

