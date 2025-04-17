package com.rainett.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    @Value("${jwt.secret:super-secret-key}")
    private String secretKey;

    public String validateTokenAndGetUsername(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey)).build()
                .verify(token)
                .getIssuer();
    }

    public String generateToken(String username) {
        return JWT.create()
                .withIssuer(username)
                .sign(Algorithm.HMAC256(secretKey));
    }
}
