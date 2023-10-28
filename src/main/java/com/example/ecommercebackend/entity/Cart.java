package com.example.ecommercebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userID;

    @Column(name = "items")
    private String items;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "total_items_count")
    private int totalItemsCount;

    @Column(name = "status")
    private String status;

    @Column(name = "payment_information")
    private String paymentInformation;

}
