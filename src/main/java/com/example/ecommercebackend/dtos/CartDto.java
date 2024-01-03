package com.example.ecommercebackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private Long customerId;
    private List<Long> productIds;
    private double totalPrice;
    private String status;
    private String paymentInformation;
}