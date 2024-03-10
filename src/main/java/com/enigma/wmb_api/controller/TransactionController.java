package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.*;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.model.request.ReportRequest;
import com.enigma.wmb_api.model.request.TransactionRequest;
import com.enigma.wmb_api.model.request.update.TransactionStatusUpdateRequest;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.model.response.TransactionResponse;
import com.enigma.wmb_api.service.TransactionService;
import com.enigma.wmb_api.service.UserCredentialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "Transaction", description = "Transaction API")
@RequestMapping(APIUrl.TRANSACTION)
public class TransactionController {
    private final TransactionService service;
    private final UserCredentialService credentialService;

    @Operation(summary = "Create Transaction")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<CommonResponse<TransactionResponse>> create(@RequestBody TransactionRequest request) {
        boolean isAdmin = credentialService.getByContext().getRole()
                .stream()
                .map(Role::getRole)
                .anyMatch(role -> role.equals(UserRole.ROLE_ADMIN));

        if (!isAdmin) {
            request.setUserId(credentialService.getByContext().getUser().getId());
        }

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
    @PreAuthorize("hasAnyRole('ADMIN')")
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

    @Operation(summary = "Update Transaction Status")
    @PostMapping(
            path = "/status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<?>> updateTransaction(@RequestBody Map<String, Object> payload) {
        TransactionStatusUpdateRequest request = TransactionStatusUpdateRequest.builder()
                .transactionStatus(TransactionStatus.valueOf(payload.get("transaction_status").toString().toUpperCase()))
                .orderId(payload.get("order_id").toString())
                .build();
        service.updateStatus(request);

        return ResponseEntity.ok(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .build());
    }

    @Operation(summary = "Get Transaction Report")
    @GetMapping("/report")
    public ResponseEntity<Resource> getReport(
            @RequestParam(name = "period", defaultValue = "DAILY") ReportPeriod period,
            @RequestParam(name ="fileType", defaultValue = "CSV") FileType fileType,
            @RequestParam(name ="isSummarized", defaultValue = "false") boolean isSummarized
    ) {
        ReportRequest request = ReportRequest.builder()
                .period(period)
                .fileType(fileType)
                .isSummarized(isSummarized)
                .build();

        Resource csvReport = service.getReport(request);
        String headerValue = String.format("inline; filename=%s", csvReport.getFilename());
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(csvReport);
    }
}
