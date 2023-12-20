package com.example.ecommercebackend.entities;

import com.example.ecommercebackend.customer.Customer;
import com.example.ecommercebackend.token.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TemporaryPassword {

    @Id
    @GeneratedValue
    public Integer id;

    @Column(unique = true)
    public String token;

    public boolean revoked;

    public boolean expired;

    public boolean used;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    public Customer customer;
}
