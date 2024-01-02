package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
