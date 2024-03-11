package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.enums.TransactionType;
import com.enigma.wmb_api.entity.TransType;
import com.enigma.wmb_api.repo.TransTypeRepo;
import com.enigma.wmb_api.service.TransTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransTypeServiceImplTest {
    @Mock private TransTypeRepo repo;
    private TransTypeService service;

    @BeforeEach
    void setUp() {
        service = new TransTypeServiceImpl(repo);
    }

    @DisplayName("getOrCreate - Positive Case")
    @Test
    public void getOrCreate_WhenExist_ShouldReturnTransType() {
        // Given
        TransType existingTransType = TransactionType.DINE_IN.getTransType();

        // Stubbing
        when(repo.findById(any())).thenReturn(Optional.of(existingTransType));

        // When
        TransType result = service.getOrCreate(TransactionType.DINE_IN);

        // Then
        assertEquals(existingTransType, result);
        verify(repo, times(1)).findById(any());
        verify(repo, never()).save(any(TransType.class));
    }

    @DisplayName("getOrCreate - Negative Case")
    @Test
    public void getOrCreate_WhenNotExist_ShouldSaveTransType(){
        // Given
        TransactionType transType = TransactionType.TAKE_AWAY;
        TransType type = transType.getTransType();

        // Stubbing
        when(repo.findById(any())).thenReturn(Optional.empty());
        when(repo.saveAndFlush(any())).thenReturn(type);

        // When
        TransType actual = service.getOrCreate(transType);

        // Then
        assertEquals(type, actual);
        verify(repo, times(1)).findById(any());
        verify(repo, times(1)).saveAndFlush(any());
    }

}