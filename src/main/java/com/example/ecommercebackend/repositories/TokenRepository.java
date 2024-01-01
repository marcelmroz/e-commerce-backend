package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
            select t from Token t inner join Customer u\s
            on t.customer.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByCustomer(Integer id);

    Optional<Token> findByToken(String token);
}
