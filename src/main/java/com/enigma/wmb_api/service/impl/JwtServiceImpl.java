package com.enigma.wmb_api.service.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.wmb_api.entity.UserCredential;
import com.enigma.wmb_api.model.response.JwtClaims;
import com.enigma.wmb_api.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final String JWT_SECRET;
    private final String ISSUER;
    private final long EXPIRATION_TIME;

    public JwtServiceImpl(
            @Value("${wmb_api.jwt.secret_key}") String jwtSecret,
            @Value("${wmb_api.jwt.issuer}") String issuer,
            @Value("${wmb_api.jwt.expiration}") long expirationTime
    ) {
        JWT_SECRET = jwtSecret;
        ISSUER = issuer;
        EXPIRATION_TIME = expirationTime;
    }

    @Override
    public String generateToken(UserCredential credential) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            return JWT.create()
                    .withSubject(credential.getId())
                    .withClaim("roles", credential.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .withIssuer(ISSUER)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(EXPIRATION_TIME)) // seconds * minutes
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating jwt");
        }
    }

    @Override
    public boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            jwtVerifier.verify(parseJwt(token));
            return true;
        } catch (JWTVerificationException e) {
            log.error("Invalid JWT Signature/Claims : {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    @Override
    public JwtClaims getClaimsFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(parseJwt(token));
            return JwtClaims.builder()
                    .userCredentialId(decodedJWT.getSubject())
                    .roles(decodedJWT.getClaim("roles").asList(String.class))
                    .build();
        } catch (JWTVerificationException e) {
            log.error("Invalid JWT Signature/Claims : {}", e.getMessage());
            return null;
        }

    }

    private String parseJwt(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

}
