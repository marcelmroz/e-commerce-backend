package com.example.ecommercebackend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private String secretKey = "yourSecretKey";

    public String generateToken(String username) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .sign(Algorithm.HMAC256(secretKey));
    }

}
