package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.entity.DinningTable;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.service.DinningTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIUrl.DINNING_TABLE)
public class DinningTableController {
    private final DinningTableService service;
    
    @GetMapping
    public ResponseEntity<CommonResponse<List<DinningTable>>> getAllTables() {
        List<DinningTable> tables = service.findAll();
        CommonResponse<List<DinningTable>> response = CommonResponse.<List<DinningTable>>
                builder()
                .data(tables)
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<CommonResponse<DinningTable>> create(@RequestBody DinningTable dinningTable) {
        service.create(dinningTable);
        CommonResponse<DinningTable> response = CommonResponse.<DinningTable>
                builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Dinning table created successfully")
                .data(dinningTable)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);  
    }

    @PutMapping
    public ResponseEntity<CommonResponse<DinningTable>> update(@RequestBody DinningTable dinningTable) {
        service.update(dinningTable);
        CommonResponse<DinningTable> response = CommonResponse.<DinningTable>
                builder()
                .statusCode(HttpStatus.OK.value())
                .message("Dinning table updated successfully")
                .data(dinningTable)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse<DinningTable>> delete(@RequestBody DinningTable dinningTable) {
        service.delete(dinningTable.getId());
        CommonResponse<DinningTable> response = CommonResponse.<DinningTable>
                builder()
                .statusCode(HttpStatus.OK.value())
                .message("Dinning table deleted successfully")
                .data(dinningTable)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<DinningTable>> getTable(@PathVariable String id) {
        DinningTable table = service.findOrFail(id);
        CommonResponse<DinningTable> response = CommonResponse.<DinningTable>
                builder()
                .data(table)
                .build();
        return ResponseEntity.ok(response);
    }
}
