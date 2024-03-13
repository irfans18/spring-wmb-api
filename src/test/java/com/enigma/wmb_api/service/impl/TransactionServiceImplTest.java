package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.enums.FileType;
import com.enigma.wmb_api.constant.enums.ReportPeriod;
import com.enigma.wmb_api.constant.enums.TransactionStatus;
import com.enigma.wmb_api.constant.enums.TransactionType;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.model.DailyReportModel;
import com.enigma.wmb_api.model.request.BillDetailRequest;
import com.enigma.wmb_api.model.request.ReportRequest;
import com.enigma.wmb_api.model.request.TransactionRequest;
import com.enigma.wmb_api.model.request.update.TransactionStatusUpdateRequest;
import com.enigma.wmb_api.model.response.TransactionResponse;
import com.enigma.wmb_api.repo.TransactionRepo;
import com.enigma.wmb_api.service.*;
import com.enigma.wmb_api.util.DateUtil;
import com.enigma.wmb_api.util.ReportUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepo repo;
    @Mock
    private BillDetailService billDetailService;
    @Mock
    private UserService userService;
    @Mock
    private DinningTableService tableService;
    @Mock
    private TransTypeService trxTypeService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private ReportUtil reportUtil;
    private TransactionService service;

    @BeforeEach
    void setUp() {
        service = new TransactionServiceImpl(repo, billDetailService, userService, tableService, trxTypeService, paymentService, reportUtil);
    }

    @Test
    void findAll_WhenSuccess_ShouldReturnListTransactionResponse() {
        // Given
        Transaction trx = Transaction.builder().trxDate(new Date()).trxType(TransactionType.TAKE_AWAY.getTransType()).build();

        // Stubbing
        when(repo.findAll()).thenReturn(List.of(trx));

        // When
        List<TransactionResponse> result = service.findAll();

        // Then
        assertAll("Should return list of TransactionResponse", () -> assertNotNull(result), () -> assertEquals(1, result.size()), () -> assertEquals(trx.getTrxDate(), result.get(0).getTrxDate()));
        verify(repo, times(1)).findAll();
    }

    @Test
    void findOrFail_WhenExist_ShouldReturnTransaction() {
        // Given
        String id = "0b26e10d-1a9a-4a2b-8ee8-606fd897508f";
        Transaction trx = Transaction.builder().id(id).trxDate(new Date()).trxType(TransactionType.TAKE_AWAY.getTransType()).build();

        // Stubbing
        when(repo.findById(any())).thenReturn(Optional.of(trx));

        // When
        Transaction result = service.findOrFail(id);

        // Then
        assertAll("Should return Transaction", () -> assertNotNull(result), () -> assertEquals(id, result.getId()));
        verify(repo, times(1)).findById(any());

    }

    @Test
    void findById_WhenExist_ShouldReturnTransactionResponse() {
        // Given
        String id = "0b26e10d-1a9a-4a2b-8ee8-606fd897508f";
        Transaction trx = Transaction.builder().id(id).trxDate(new Date()).trxType(TransactionType.TAKE_AWAY.getTransType()).build();

        // Stubbing
        when(repo.findById(any())).thenReturn(Optional.of(trx));

        // When
        TransactionResponse result = service.findById(id);

        // Then
        assertAll("Should return TransactionResponse", () -> assertNotNull(result), () -> assertEquals(trx.getId(), result.getId()));
        verify(repo, times(1)).findById(any());
    }

    @Test
    void create_WhenSuccess_ShouldReturnTransactionResponse() {
        // Given
        User user = User.builder()
                .id("6c236754-51fc-4acf-a41e-ad7618d92b1b")
                .name("GaryMusa")
                .build();

        Transaction trx = Transaction.builder()
                .id("2f52f3ea-37d6-4c8b-8f4d-443ad7c764fa")
                .trxDate(new Date())
                .build();

        Menu menu = Menu.builder()
                .name("Spageti Carbonara")
                .price(10000)
                .build();
        BillDetail detail = BillDetail.builder()
                .id("58a40dfd-0c7b-412b-b811-c609ba733fa2")
                .qty(10)
                .menu(menu)
                .transaction(trx)
                .build();

        Payment payment = Payment.builder()
                .transactionStatus(TransactionStatus.ORDERED)
                .redirectUrl("www.youtub.com")
                .token("73a81a3d-3d03-4ac9-ae0f-c329037f3660")
                .id("03e5eefb-e301-4c7a-a3c9-a0172250fa67")
                .build();

        DinningTable dinningTable = DinningTable.builder()
                .id("64c0611f-8a90-4d72-b40f-03f21c5c9fca")
                .name("Table 1")
                .build();

        TransactionRequest request = TransactionRequest.builder()
                .userId(user.getId())
                .dinningTableId(dinningTable.getId())
                .billDetails(
                        List.of(
                            BillDetailRequest
                                .builder()
                                .menuId("1")
                                .qty(2)
                                .build()
                        )
                ).build();




        // Stubbing
        when(userService.findOrFail(any())).thenReturn(user);
        when(tableService.findOrFail(any())).thenReturn(dinningTable);
        when(trxTypeService.getOrCreate(any())).thenReturn(TransactionType.DINE_IN.getTransType());
        when(billDetailService.create(any(), any())).thenReturn(detail);
        when(paymentService.create(any())).thenReturn(payment);
        trx.setBillDetails(List.of(detail));
        trx.setPayment(payment);

        // When
        TransactionResponse result = service.create(request);

        // Then
        assertAll(
                "Should return TransactionResponse",
                () -> assertNotNull(result),
                () -> assertEquals(trx.getPayment().getTransactionStatus(), result.getPayment().getTransactionStatus()),
                () -> assertEquals(trx.getBillDetails().size(), result.getBillDetails().size()),
                () -> assertEquals(user.getId(), result.getUserId()),
                () -> assertEquals(dinningTable.getId(), result.getDinningTableId())
        );

        assertAll(
                "Sould invoke methods",
                () -> verify(userService, times(1)).findOrFail(any()),
                () -> verify(tableService, times(1)).findOrFail(any()),
                () -> verify(trxTypeService, times(1)).getOrCreate(any()),
                () -> verify(billDetailService, times(1)).create(any(), any()),
                () -> verify(paymentService, times(1)).create(any())
        );
    }

    @Test
    void updateStatus() {
        // Given
        String id = "0b26e10d-1a9a-4a2b-8ee8-606fd897508f";
        TransactionStatusUpdateRequest request = TransactionStatusUpdateRequest.builder().orderId(id).transactionStatus(TransactionStatus.CANCEL).build();
        Transaction trx = Transaction.builder().id(id).trxDate(new Date()).trxType(TransactionType.TAKE_AWAY.getTransType()).payment(Payment.builder().transactionStatus(TransactionStatus.ORDERED).build()).build();

        // Stubbing
        when(repo.findById(any())).thenReturn(Optional.of(trx));
        trx.getPayment().setTransactionStatus(request.getTransactionStatus());

        // When
        service.updateStatus(request);

        verify(repo, times(1)).findById(any());
    }

    @Test
    void getReport_WhenSuccess_ShouldReturnResource() {
        ReportRequest request = ReportRequest.builder().fileType(FileType.CSV).period(ReportPeriod.DAILY).isSummarized(false).build();

        List<Transaction> transactions = List.of(Transaction.builder().id("0b26e10d-1a9a-4a2b-8ee8-606fd897508f").trxDate(new Date()).trxType(TransactionType.TAKE_AWAY.getTransType()).billDetails(List.of(BillDetail.builder().menu(Menu.builder().name("Burger").build()).price(100).qty(1).build(), BillDetail.builder().price(200).menu(Menu.builder().name("Burger").build()).qty(2).build())).build());

        // Stubbing
        when(repo.findAllByTrxDateBetween(any(), any())).thenReturn(transactions);

        // When
        service.getReport(request);

        // Then
        verify(repo, times(1)).findAllByTrxDateBetween(any(), any());

    }
}