package com.example.ecommercebackend.repositories;


import com.example.ecommercebackend.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}