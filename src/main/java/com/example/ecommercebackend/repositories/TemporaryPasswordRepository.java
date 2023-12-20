package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.entities.TemporaryPassword;
import com.example.ecommercebackend.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TemporaryPasswordRepository extends JpaRepository<TemporaryPassword, Integer> {

    @Query(value = """
            select t from TemporaryPassword t inner join Customer u\s
            on t.customer.id = u.id\s
            where u.id = :id and (t.expired = false and t.revoked = false)\s
            """)
    TemporaryPassword findValidTokenByCustomer(Integer id);

    @Query(value = """
            select t from TemporaryPassword t inner join Customer u\s
            on t.customer.id = u.id\s
            where u.id = :id and ((t.expired = false and t.revoked = false) or t.used = false)\s
            """)
    TemporaryPassword findAgainValid(Integer id);
    Optional<TemporaryPassword> findByToken(String token);

}
