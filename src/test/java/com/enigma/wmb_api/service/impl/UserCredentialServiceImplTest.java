package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.UserCredential;
import com.enigma.wmb_api.repo.UserCredentialRepo;
import com.enigma.wmb_api.service.UserCredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserCredentialServiceImplTest {

    @Mock
    private UserCredentialRepo repo;
    private UserCredentialService service;

    @BeforeEach
    void setUp() {
        service = new UserCredentialServiceImpl(repo);
    }

    @Test
    void findOrFail_WhenFound_ShouldReturnCredential() {
        // Given
        UserCredential credential = UserCredential.builder()
                .id("1")
                .build();

        // Stubbing
        when(repo.findById(any())).thenReturn(Optional.of(credential));

        // When
        UserCredential result = service.findOrFail(credential.getId());

        // Then
        assertEquals(credential, result);
        verify(repo, times(1)).findById(any());
    }

    @Test
    void getByContext_WhenFound_ShouldReturnUserCredential() {
        // Given
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserCredential credential =  UserCredential.builder().id("user_id").build();
        when(authentication.getPrincipal()).thenReturn(credential.getId());
        when(repo.findById(any())).thenReturn(Optional.of(credential));

        // When
        UserCredential result = service.getByContext();

        // Then
        assertEquals(credential, result);
        verify(repo, times(1)).findById(any());
    }

    @Test
    void loadUserByUsername_WhenFound_ShouldReturnUserDetails() {
        // Given
        UserCredential credential = UserCredential.builder()
                .id("1")
                .username("admin")
                .build();

        // Stubbing
        when(repo.findFirstByUsername(any())).thenReturn(Optional.of(credential));

        // When
        UserDetails result = service.loadUserByUsername(credential.getUsername());

        // Then
        assertEquals(credential, result);
        verify(repo, times(1)).findFirstByUsername(any());
    }
}