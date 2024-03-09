package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.model.request.TransactionRequest;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.model.response.TransactionResponse;
import com.enigma.wmb_api.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "Transaction", description = "Transaction API")
@RequestMapping(APIUrl.TRANSACTION)
@PreAuthorize("hasAnyRole('ADMIN')")
public class TransactionController {
    private final TransactionService service;

    @Operation(summary = "Create Transaction")
    @PostMapping
    public ResponseEntity<CommonResponse<TransactionResponse>> create(@RequestBody TransactionRequest request) {
        TransactionResponse transactionResponse = service.create(request);

        CommonResponse<TransactionResponse> response = CommonResponse
                .<TransactionResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(transactionResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get Transaction By Id")
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<TransactionResponse>> getTransaction(@PathVariable String id) {
        TransactionResponse transactionResponse = service.findById(id);
        CommonResponse<TransactionResponse> response = CommonResponse
                .<TransactionResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactionResponse)
                .build();
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Get All Transaction")
    @GetMapping
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> getAllTransactions() {
        List<TransactionResponse> transactionResponses = service.findAll();
        CommonResponse<List<TransactionResponse>> response = CommonResponse
                .<List<TransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactionResponses)
                .build();
        return ResponseEntity.ok(response);
    }
}
