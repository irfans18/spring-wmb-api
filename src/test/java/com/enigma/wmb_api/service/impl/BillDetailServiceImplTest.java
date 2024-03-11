package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.BillDetail;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.entity.Transaction;
import com.enigma.wmb_api.model.request.BillDetailRequest;
import com.enigma.wmb_api.repo.BillDetailRepo;
import com.enigma.wmb_api.service.BillDetailService;
import com.enigma.wmb_api.service.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BillDetailServiceImplTest {
    @Mock private BillDetailRepo repo;
    @Mock private MenuService menuService;
    private BillDetailService service;

    @BeforeEach
    void setUp() {
        service = new BillDetailServiceImpl(repo, menuService);
    }

    @Test
    void create_WhenSuccess_ShouldReturnBillDetail() {
        // Given
        BillDetailRequest request = BillDetailRequest.builder()
                .menuId("valid_menu_id")
                .qty(2)
                .build();
        Transaction trx = new Transaction();

        Menu menu = Menu.builder()
                .id("valid_menu_id")
                .name("Menu 1")
                .price(10)
                .build();

        BillDetail billDetail = BillDetail.builder()
                .id("1")
                .menu(menu)
                .qty(2)
                .transaction(trx)
                .price(20)
                .build();

        // Stubbing
        when(menuService.findOrFail(any())).thenReturn(menu);
        when(repo.saveAndFlush(any())).thenReturn(billDetail);

        // When
        BillDetail result = service.create(request, trx);

        // Then
        assertAll(
                "Check bill detail",
                () -> assertNotNull(result),
                () -> assertEquals("1", result.getId()),
                () -> assertEquals(menu, result.getMenu()),
                () -> assertEquals(2, result.getQty().intValue()),
                () -> assertEquals(20, result.getPrice()),
                () -> assertEquals(trx, result.getTransaction())
        );

        assertAll(
                "verify method invoked",
                () -> verify(menuService, times(1)).findOrFail(any()),
                () -> verify(repo, times(1)).saveAndFlush(any())
        );
    }
}