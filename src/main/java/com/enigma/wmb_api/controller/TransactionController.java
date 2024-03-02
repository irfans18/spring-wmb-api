package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.model.request.TransactionRequest;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.model.response.TransactionResponse;
import com.enigma.wmb_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIUrl.TRANSACTION)
public class TransactionController {
    private final TransactionService service;

    @PostMapping
    public ResponseEntity<CommonResponse<TransactionResponse>> create(@RequestBody TransactionRequest request) {
        TransactionResponse transactionResponse = service.create(request);

        CommonResponse<TransactionResponse> response = CommonResponse
                .<TransactionResponse>builder()
                .data(transactionResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<TransactionResponse>> getTransaction(@PathVariable String id) {
        TransactionResponse transactionResponse = service.findById(id);
        CommonResponse<TransactionResponse> response = CommonResponse
                .<TransactionResponse>builder()
                .data(transactionResponse)
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> getAllTransactions() {
        List<TransactionResponse> transactionResponses = service.findAll();
        CommonResponse<List<TransactionResponse>> response = CommonResponse
                .<List<TransactionResponse>>builder()
                .data(transactionResponses)
                .build();
        return ResponseEntity.ok(response);
    }
}
