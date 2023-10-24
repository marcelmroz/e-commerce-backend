package com.example.ecommercebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name= "price")
    private double price;

    @Column(name = "color")
    private String color;

    @Column(name = "category")
    private String category;

    @Column(name = "size")
    private String size;

    @Column(name = "stock_quantity")
    private int stockQuantity;
}
