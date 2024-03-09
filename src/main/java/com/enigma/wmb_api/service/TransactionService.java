package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.Transaction;
import com.enigma.wmb_api.model.request.TransactionRequest;
import com.enigma.wmb_api.model.request.update.TransactionStatusUpdateRequest;
import com.enigma.wmb_api.model.response.TransactionResponse;

import java.util.List;

public interface TransactionService {

    List<TransactionResponse> findAll();

    Transaction findOrFail(String id);
    TransactionResponse findById(String id);

    TransactionResponse create(TransactionRequest request);

    void updateStatus(TransactionStatusUpdateRequest request);
}
