package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.constant.TransactionType;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.model.request.TransactionRequest;
import com.enigma.wmb_api.model.request.update.TransactionStatusUpdateRequest;
import com.enigma.wmb_api.model.response.BillDetailResponse;
import com.enigma.wmb_api.model.response.PaymentResponse;
import com.enigma.wmb_api.model.response.TransactionResponse;
import com.enigma.wmb_api.repo.TransactionRepo;
import com.enigma.wmb_api.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepo repo;
    private final BillDetailService billDetailService;
    private final UserService userService;
    private final DinningTableService tableService;
    private final TransTypeService trxTypeService;
    private final PaymentService paymentService;

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> findAll() {
        return repo.findAll().stream().map(trx -> TransactionResponse
                .builder()
                .trxDate(trx.getTrxDate())
                .userId(trx.getUser().getId())
                .dinningTableId(trx.getDinningTable().getName())
                .transType(trx.getTrxType().getDescription())
                .build()
        ).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Transaction findOrFail(String id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse findById(String id) {
        return repo.findById(id).map(trx -> TransactionResponse
                .builder()
                .userId(trx.getUser().getId())
                .dinningTableId(trx.getDinningTable().getName())
                .transType(trx.getTrxType().getDescription())
                .trxDate(trx.getTrxDate())
                .build()
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse create(TransactionRequest request) {
        User user = null;
        DinningTable table = null;
        TransType trxType;
        if (request.getUserId() != null) {
            user = userService.findOrFail(request.getUserId());
        }
        if (request.getDinningTableId() != null) {
            table = tableService.findOrFail(request.getDinningTableId());
            trxType = trxTypeService.findOrFail(TransactionType.DINE_IN.value);
        } else {
            trxType = trxTypeService.findOrFail(TransactionType.TAKE_AWAY.value);
        }

        Transaction transaction = Transaction
                .builder()
                .trxDate(new Date())
                .user(user)
                .dinningTable(table)
                .trxType(trxType)
                .build();


        List<BillDetail> billDetails = new ArrayList<>();
        request.getBillDetails().forEach(req -> {
            BillDetail detail = billDetailService.create(req, transaction);
            billDetails.add(detail);
        });

        transaction.setBillDetails(billDetails);

        Payment payment = paymentService.create(transaction);
        transaction.setPayment(payment);


        return TransactionResponse
                .builder()
                .trxDate(transaction.getTrxDate())
                .userId(request.getUserId())
                .transType(transaction.getTrxType().getDescription())
                .dinningTableId(table == null ? null : table.getId())
                .billDetails(billDetails.stream().map(this::mapToResponse).toList())
                .payment(mapToResponse(payment))
                .build();
    }

    @Override
    public void updateStatus(TransactionStatusUpdateRequest request) {
        Transaction trx = repo.findById(request.getOrderId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        trx.getPayment().setTransactionStatus(request.getTransactionStatus());
    }

    private BillDetailResponse mapToResponse(BillDetail billDetail) {
        return BillDetailResponse.builder()
                .id(billDetail.getId())
                .qty(billDetail.getQty())
                .price(billDetail.getPrice())
                .menuId(billDetail.getMenu().getId())
                .build();
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse
                .builder()
                .id(payment.getId())
                .token(payment.getToken())
                .redirectUrl(payment.getRedirectUrl())
                .transactionStatus(payment.getTransactionStatus())
                .build();
    }
}
