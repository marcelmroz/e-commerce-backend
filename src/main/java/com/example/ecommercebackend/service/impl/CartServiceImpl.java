package com.example.ecommercebackend.service.impl;

import com.example.ecommercebackend.dto.CartDto;
import com.example.ecommercebackend.dto.CustomerDto;
import com.example.ecommercebackend.entity.Cart;
import com.example.ecommercebackend.exception.ResourceNotFoundException;
import com.example.ecommercebackend.mapper.CartMapper;
import com.example.ecommercebackend.mapper.CustomerMapper;
import com.example.ecommercebackend.repository.CartRepository;
import com.example.ecommercebackend.service.CartService;
import com.example.ecommercebackend.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CustomerService customerService;

    @Override
    public CartDto createCart(CartDto cartDto) {
        Cart cart = CartMapper.mapToCart(cartDto);
        Cart savedCart = cartRepository.save(cart);
        return CartMapper.mapToCartDto(savedCart);
    }

    @Override
    public CartDto getCartById(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart with given ID: " + cartId + " does not exist."));
        return CartMapper.mapToCartDto(cart);
    }

    @Override
    public List<CartDto> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream().map(CartMapper::mapToCartDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartDto updateCart(Long cartId, CartDto updatedCart) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart with given ID: " + cartId + " does not exist."));

        if (updatedCart.getCustomerId() != null) {
            Long customerId = updatedCart.getCustomerId();
            CustomerDto customerDto = customerService.getCustomerById(customerId);
            if (customerDto != null) {
                cart.setCustomer(CustomerMapper.mapToCustomer(customerDto));
            } else {
                throw new ResourceNotFoundException("Customer with ID: " + customerId + " does not exist.");
            }
        }

        if (updatedCart.getTotalPrice() > 0) {
            cart.setTotalPrice(updatedCart.getTotalPrice());
        }

        if (updatedCart.getStatus() != null && !updatedCart.getStatus().isEmpty()) {
            cart.setStatus(updatedCart.getStatus());
        }

        if (updatedCart.getPaymentInformation() != null && !updatedCart.getPaymentInformation().isEmpty()) {
            cart.setPaymentInformation(updatedCart.getPaymentInformation());
        }

        Cart updatedCartObj = cartRepository.save(cart);
        return CartMapper.mapToCartDto(updatedCartObj);
    }


    @Override
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
