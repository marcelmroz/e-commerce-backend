package com.example.ecommercebackend.service;

import com.example.ecommercebackend.dto.CartDto;

import java.util.List;

public interface CartService {
    CartDto createCart(CartDto cartDto);

    CartDto getCartById(Long cartId);

    List<CartDto> getAllCarts();

    CartDto updateCart(Long cartId, CartDto updatedCart);

    void deleteCart(Long cartId);
}
