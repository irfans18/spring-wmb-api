package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.enums.TransactionStatus;
import com.enigma.wmb_api.constant.enums.TransactionType;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.model.request.payment.PaymentDetailRequest;
import com.enigma.wmb_api.model.request.payment.PaymentItemDetailRequest;
import com.enigma.wmb_api.model.request.payment.PaymentRequest;
import com.enigma.wmb_api.repo.PaymentRepo;
import com.enigma.wmb_api.service.PaymentService;
import com.enigma.wmb_api.util.PaymentClientUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @Mock
    private PaymentRepo repo;
    @Mock
    private PaymentClientUtil client;
    private PaymentService service;

    @BeforeEach
    void setUp() {
        service = new PaymentServiceImpl(repo, client);
    }

    @Test
    void create_WhenSuccess_ShouldReturnPayment()  {
        // Given
        Transaction transaction = Transaction.builder()
                .id("0cd4a060-efb2-404a-8717-c1fa6b25b080")
                .trxDate(new Date())
                // .trxType(TransactionType.TAKE_AWAY.getTransType())
                .user(User.builder()
                        .id("38c14c8b-0e2e-4880-ace1-f008e3d4928b")
                        .build()
                )
                .billDetails(List.of(
                        BillDetail.builder()
                                .id("a7674f7a-2473-47f3-b395-59f94ae841ab")
                                .menu(Menu.builder()
                                        .id("0cd4a060-efb2-404a-8717-c1fa6b25b080")
                                        .name("Nasi Goreng")
                                        .price(20)
                                        .build()
                                )
                                .price(20)
                                .qty(1)
                                .build(),
                        BillDetail.builder()
                                .id("8ac854c1-72bb-4691-87d0-24c7664f12c3")
                                .menu(Menu.builder()
                                        .id("0cd4a060-efb2-404a-8717-c1fa6b25b080")
                                        .name("Nasi Uduk")
                                        .price(20)
                                        .build()
                                )
                                .price(20)
                                .qty(1)
                                .build()
                ))
                .build();


        PaymentRequest expectedPaymentRequest = PaymentRequest.builder()
                .paymentDetail(PaymentDetailRequest.builder()
                        .orderId(transaction.getId())
                        .amount(20)
                        .build())
                .paymentItemDetails(List.of(
                        PaymentItemDetailRequest.builder()
                                .price(20)
                                .quantity(1)
                                .name("Nasi Goreng")
                                .build(),
                        PaymentItemDetailRequest.builder()
                                .price(20)
                                .quantity(1)
                                .name("Nasi Uduk")
                                .build())
                )
                .paymentMethod(List.of("shopeepay", "gopay"))
                .build();

        ResponseEntity<Map<String, String>> responseEntity = ResponseEntity.ok(
                Map.of(
                        "token", "token123",
                        "redirect_url", "https://example.com"
                )
        );

        Payment payment = Payment.builder()
                .token("token123")
                .redirectUrl("https://example.com")
                .transactionStatus(TransactionStatus.ORDERED)
                .build();

        // Stubbing
        when(client.requestPayment(any())).thenReturn(responseEntity.getBody());
        when(repo.saveAndFlush(any())).thenReturn(payment);

        // When
        Payment actualPayment = service.create(transaction);

        // Then
        assertEquals("token123", actualPayment.getToken());
        assertEquals("https://example.com", actualPayment.getRedirectUrl());
        assertEquals(TransactionStatus.ORDERED, actualPayment.getTransactionStatus());
        verify(client, times(1)).requestPayment(any());
        verify(repo, times(1)).saveAndFlush(any());
    }
}