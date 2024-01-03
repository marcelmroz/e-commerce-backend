package com.example.ecommercebackend.services;

import com.example.ecommercebackend.dtos.CartDto;

import java.util.List;

public interface CartService {
    CartDto createCart(CartDto cartDto);

    CartDto getCartById(Long cartId);

    List<CartDto> getAllCarts();

    CartDto updateCart(Long cartId, CartDto updatedCart);

    void deleteCart(Long cartId);
}