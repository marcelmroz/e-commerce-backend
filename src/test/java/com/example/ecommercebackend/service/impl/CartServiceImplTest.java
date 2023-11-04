package com.example.ecommercebackend.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.ecommercebackend.entity.Cart;
import com.example.ecommercebackend.repository.CartRepository;
import com.example.ecommercebackend.service.CustomerService;
import com.example.ecommercebackend.dto.CartDto;
import com.example.ecommercebackend.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks // this will inject the mocks above into the CartServiceImpl
    private CartServiceImpl cartService;

    @Test
    public void createCartTest() {
        // Arrange
        Cart cart = new Cart();
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartDto cartDto = new CartDto();

        // Act
        CartDto savedCartDto = cartService.createCart(cartDto);

        // Assert
        assertNotNull(savedCartDto);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    public void getCartByIdTest() {
        // Arrange
        Cart cart = new Cart();
        cart.setId(1L);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        // Act
        CartDto cartDto = cartService.getCartById(1L);

        // Assert
        assertNotNull(cartDto);
        assertEquals(1L, cartDto.getId());
    }

    @Test
    public void updateCartTest() {
        // Arrange
        Cart existingCart = new Cart();
        existingCart.setId(1L);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(existingCart);

        CartDto updatedCartDto = new CartDto();
        updatedCartDto.setId(1L);

        // Act
        CartDto result = cartService.updateCart(1L, updatedCartDto);

        // Assert
        assertNotNull(result);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    public void deleteCartTest() {
        // Arrange
        doNothing().when(cartRepository).deleteById(1L);

        // Act
        cartService.deleteCart(1L);

        // Assert
        verify(cartRepository).deleteById(1L);
    }
}
