package com.enigma.wmb_api.service.impl;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.enigma.wmb_api.constant.enums.UserRole;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.entity.UserCredential;
import com.enigma.wmb_api.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    private String JWT_SECRET;
    private String ISSUER;
    private long EXPIRATION_TIME;
    private JwtService service;
    @BeforeEach
    void setUp() {
        JWT_SECRET = "secret";
        ISSUER = "issuer";
        EXPIRATION_TIME = 1000L;
        service = new JwtServiceImpl(JWT_SECRET, ISSUER, EXPIRATION_TIME);
    }

    @Test
    void generateToken_WhenSuccess_ShouldReturnToken() {
        // Given
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("1", UserRole.ROLE_ADMIN));
        roles.add(new Role("2", UserRole.ROLE_CUSTOMER));
        UserCredential credential = UserCredential.builder()
                .id("1")
                .role(roles)
                .build();

        // When
        String token = service.generateToken(credential);

        // Then
        assertNotNull(token);
    }
}