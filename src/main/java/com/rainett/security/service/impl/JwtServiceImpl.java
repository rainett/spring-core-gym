package com.rainett.security.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rainett.model.User;
import com.rainett.security.TokenUserDto;
import com.rainett.security.service.JwtService;
import java.time.Instant;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtServiceImpl implements JwtService {
    private static final String AUTHORITIES_KEY = "authorities";

    @Value("${security.jwt.secret:super-secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration:3600}")
    private Long expirationTime;

    @Override
    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim(AUTHORITIES_KEY, new ArrayList<>(user.getAuthorities()))
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(expirationTime))
                .sign(Algorithm.HMAC256(secretKey));
    }

    @Override
    public TokenUserDto validateAndParse(String token) {
        DecodedJWT decodedToken = JWT.require(Algorithm.HMAC256(secretKey)).build()
                .verify(token);
        return parseToken(token, decodedToken);
    }

    private static TokenUserDto parseToken(String token, DecodedJWT decodedToken) {
        TokenUserDto userDto = new TokenUserDto();
        userDto.setUsername(decodedToken.getSubject());
        userDto.setToken(token);
        Claim authoritiesClaim = decodedToken.getClaim(AUTHORITIES_KEY);
        authoritiesClaim.asList(String.class).forEach(userDto::addAuthority);
        return userDto;
    }
}
