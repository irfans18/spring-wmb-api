package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.*;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.model.DailyReportModel;
import com.enigma.wmb_api.model.request.ReportRequest;
import com.enigma.wmb_api.model.request.TransactionRequest;
import com.enigma.wmb_api.model.request.update.TransactionStatusUpdateRequest;
import com.enigma.wmb_api.model.response.BillDetailResponse;
import com.enigma.wmb_api.model.response.PaymentResponse;
import com.enigma.wmb_api.model.response.TransactionResponse;
import com.enigma.wmb_api.repo.TransactionRepo;
import com.enigma.wmb_api.service.*;
import com.enigma.wmb_api.util.FileUtil;
import com.enigma.wmb_api.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepo repo;
    private final BillDetailService billDetailService;
    private final UserService userService;
    private final DinningTableService tableService;
    private final TransTypeService trxTypeService;
    private final PaymentService paymentService;
    private final FileUtil fileUtil;

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

    @Override
    public Resource getReport(ReportRequest request) {
        Date startDate = DateUtil.getDateRange(request.getPeriod()).startDate();
        Date endDate = DateUtil.getDateRange(request.getPeriod()).endDate();
        List<Transaction> allByTrxDateBetween = repo.findAllByTrxDateBetween(startDate, endDate);

        List<DailyReportModel> dailyReport = new ArrayList<>();
        for (int i = 0; i < allByTrxDateBetween.size(); i++) {
            Transaction transaction = allByTrxDateBetween.get(i);
            DailyReportModel daily = mapToReport(transaction);
            if (i > 0 &&
                request.isSummarized() &&
                (dailyReport.get(dailyReport.size() - 1).getTrxDate().compareTo(daily.getTrxDate()) == 0)
            ) {
                dailyReport.get(dailyReport.size() -1).setTotal(dailyReport.get(dailyReport.size() - 1).getTotal() + daily.getTotal());
            }else dailyReport.add(daily);
        }

        String filename = request.getPeriod().name().toLowerCase() + "_report_" + LocalDate.now() + ".csv";
        return fileUtil.generateCsv(dailyReport, filename, request.isSummarized());
    }

    private static DailyReportModel mapToReport(Transaction transaction) {
        return DailyReportModel.builder()
                .trxId(transaction.getId())
                .userId(transaction.getUser() == null ? "Guest" : transaction.getUser().getId())
                .description(transaction.getTrxType().getDescription())
                .tableName(transaction.getDinningTable() == null ? null : transaction.getDinningTable().getName())
                .trxDate(DateUtil.parseDate(transaction.getTrxDate(), "yyyy-MM-dd"))
                .total(
                        transaction.getBillDetails().stream().map(BillDetail::getPrice).reduce(0, Integer::sum)
                )
                .status(transaction.getPayment() == null ? null : transaction.getPayment().getTransactionStatus())
                .build();
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
