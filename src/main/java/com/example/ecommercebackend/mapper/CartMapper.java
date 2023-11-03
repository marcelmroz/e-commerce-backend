package com.example.ecommercebackend.mapper;

import com.example.ecommercebackend.dto.CartDto;
import com.example.ecommercebackend.entity.Cart;
import com.example.ecommercebackend.entity.Customer;
import com.example.ecommercebackend.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartDto mapToCartDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setCustomerId(cart.getCustomer().getId());
        cartDto.setProductIds(cart.getProducts().stream().map(Product::getId).collect(Collectors.toList()));
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
        customer.setId(cartDto.getCustomerId());
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
