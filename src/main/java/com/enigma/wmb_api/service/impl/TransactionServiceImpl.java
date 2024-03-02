package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.TransactionType;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.model.request.TransactionRequest;
import com.enigma.wmb_api.model.response.BillDetailResponse;
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
@Transactional(rollbackFor = Exception.class)
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepo repo;
    private final BillDetailService billDetailService;
    private final UserService userService;
    private final DinningTableService tableService;
    private final TransTypeService trxTypeService;
    private final MenuService menuService;

    @Override
    public List<TransactionResponse> findAll() {
        return repo.findAll().stream().map(trx -> TransactionResponse
                .builder()
                .trxDate(trx.getTrxDate())
                .userId(trx.getUser().getId())
                .dinningTableName(trx.getDinningTable().getName())
                .transType(trx.getTrxType().getDescription())
                .build()
        ).toList();
    }

    @Override
    public Transaction findOrFail(String id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }

    @Override
    public TransactionResponse findById(String id) {
        return repo.findById(id).map(trx -> TransactionResponse
                .builder()
                .userId(trx.getUser().getId())
                .dinningTableName(trx.getDinningTable().getName())
                .transType(trx.getTrxType().getDescription())
                .trxDate(trx.getTrxDate())
                .build()
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }

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


        List<BillDetailResponse> billDetails = new ArrayList<>();
        request.getBillDetails().forEach(req -> {
            BillDetailResponse response = billDetailService.create(req, transaction);
            billDetails.add(response);
        });



        return TransactionResponse
                .builder()
                .trxDate(transaction.getTrxDate())
                .userId(request.getUserId())
                .transType(transaction.getTrxType().getDescription())
                .dinningTableName(table == null ? null : table.getName())
                .billDetails(billDetails)
                .build();
    }
}
