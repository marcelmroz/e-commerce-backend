package com.example.ecommercebackend.mappers;


import com.example.ecommercebackend.customer.Customer;
import com.example.ecommercebackend.dtos.CartDto;
import com.example.ecommercebackend.entities.Cart;

public class CartMapper {

    public static CartDto mapToCartDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        if (cart.getCustomer() != null) {
            cartDto.setCustomerId(cart.getCustomer().getId());
        }
        cartDto.setProductIds(cart.getProductIds());
        cartDto.setEmail(cart.getEmail());
        cartDto.setTotalPrice(cart.getTotalPrice());
        cartDto.setStatus(cart.getStatus());
        cartDto.setPaymentInformation(cart.getPaymentInformation());
        return cartDto;
    }

    public static Cart mapToCart(CartDto cartDto) {
        Cart cart = new Cart();
        cart.setId(cartDto.getId());

        // Set the customer for the cart
        if (cartDto.getCustomerId() != null) {
            Customer customer = new Customer();
            customer.setId(cartDto.getCustomerId());
            cart.setCustomer(customer);
        }

        // Set product IDs
        cart.setProductIds(cartDto.getProductIds());
        cart.setEmail(cartDto.getEmail());
        cart.setTotalPrice(cartDto.getTotalPrice());
        cart.setStatus(cartDto.getStatus());
        cart.setPaymentInformation(cartDto.getPaymentInformation());
        return cart;
    }
}