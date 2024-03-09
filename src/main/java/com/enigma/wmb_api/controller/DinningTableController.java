package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.entity.DinningTable;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.service.DinningTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "Dinning Table", description = "Dinning Table API")
@RequestMapping(APIUrl.DINNING_TABLE)
public class DinningTableController {
    private final DinningTableService service;

    @Operation(summary = "Get All Dinning Table")
    @GetMapping
    public ResponseEntity<CommonResponse<List<DinningTable>>> getAllTables() {
        List<DinningTable> tables = service.findAll();
        CommonResponse<List<DinningTable>> response = CommonResponse.<List<DinningTable>>
                builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(tables)
                .build();
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Create Dinning Table")
    @PostMapping
    public ResponseEntity<CommonResponse<DinningTable>> create(@RequestBody DinningTable dinningTable) {
        service.create(dinningTable);
        CommonResponse<DinningTable> response = CommonResponse.<DinningTable>
                builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(dinningTable)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);  
    }

    @Operation(summary = "Update Dinning Table")
    @PutMapping
    public ResponseEntity<CommonResponse<DinningTable>> update(@RequestBody DinningTable dinningTable) {
        service.update(dinningTable);
        CommonResponse<DinningTable> response = CommonResponse.<DinningTable>
                builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(dinningTable)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Delete Dinning Table")
    @DeleteMapping
    public ResponseEntity<CommonResponse<?>> delete(@RequestBody DinningTable dinningTable) {
        service.delete(dinningTable.getId());
        CommonResponse<?> response = CommonResponse.
                builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
