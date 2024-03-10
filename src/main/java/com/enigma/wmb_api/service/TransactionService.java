package com.enigma.wmb_api.service;

import com.enigma.wmb_api.constant.FileType;
import com.enigma.wmb_api.constant.ReportPeriod;
import com.enigma.wmb_api.entity.Transaction;
import com.enigma.wmb_api.model.request.ReportRequest;
import com.enigma.wmb_api.model.request.TransactionRequest;
import com.enigma.wmb_api.model.request.update.TransactionStatusUpdateRequest;
import com.enigma.wmb_api.model.response.TransactionResponse;
import org.springframework.core.io.Resource;

import java.util.List;

public interface TransactionService {

    List<TransactionResponse> findAll();

    Transaction findOrFail(String id);
    TransactionResponse findById(String id);

    TransactionResponse create(TransactionRequest request);

    void updateStatus(TransactionStatusUpdateRequest request);

    Resource getReport(ReportRequest request);
}
