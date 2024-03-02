package com.enigma.enigma_shop.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.enigma.enigma_shop.dto.response.JwtClaims;
import com.enigma.enigma_shop.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class JwtServiceImpl implements JwtService {
    private final String JWT_SECRET;
    private final String ISSUER;

    public JwtServiceImpl(
            @Value("${enigma_shop.jwt.secret_key}") String jwtSecret,
            @Value("${enigma_shop.jwt.issuer}") String issuer
    ) {
        JWT_SECRET = jwtSecret;
        ISSUER = issuer;
    }

    @Override
    public String generateToken() {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(60 * 15)) // seconds * minutes
                    .sign(algorithm);
        }catch (JWTCreationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creatin jwt");
        }
    }

    @Override
    public boolean verifyToken(String token) {
        return false;
    }

    @Override
    public JwtClaims getClaimsFromToken(String token) {
        return null;
    }
}
