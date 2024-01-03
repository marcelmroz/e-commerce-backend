package com.example.ecommercebackend.mappers;


import com.example.ecommercebackend.customer.Customer;
import com.example.ecommercebackend.dtos.CartDto;
import com.example.ecommercebackend.entities.Cart;
import com.example.ecommercebackend.entities.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartDto mapToCartDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        if (cart.getCustomer() != null) {
            cartDto.setCustomerId(Long.valueOf(cart.getCustomer().getId()));
        }
        cartDto.setProductIds(cart.getProducts() != null ? cart.getProducts().stream().map(Product::getId).collect(Collectors.toList()) : null);
        cartDto.setTotalPrice(cart.getTotalPrice());
        cartDto.setStatus(cart.getStatus());
        cartDto.setPaymentInformation(cart.getPaymentInformation());
        return cartDto;
    }

    public static Cart mapToCart(CartDto cartDto) {
        Cart cart = new Cart();
        cart.setId(cartDto.getId());

        // Set the customer for the cart
        Customer customer = new Customer();
        customer.setId(Math.toIntExact(cartDto.getCustomerId()));
        cart.setCustomer(customer);

        // Handle products in the cart
        List<Product> products = new ArrayList<>();
        // Check if product IDs are null and initialize if necessary
        List<Long> productIds = cartDto.getProductIds() != null ? cartDto.getProductIds() : new ArrayList<>();
        for (Long productId : productIds) {
            Product product = new Product();
            product.setId(productId);
            products.add(product);
        }
        cart.setProducts(products);

        cart.setTotalPrice(cartDto.getTotalPrice());
        cart.setStatus(cartDto.getStatus());
        cart.setPaymentInformation(cartDto.getPaymentInformation());
        return cart;
    }
}
