package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.entity.DinningTable;
import com.enigma.wmb_api.model.request.DinningTableRequest;
import com.enigma.wmb_api.model.request.update.DinningTableNewOrUpdateRequest;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.model.response.DinningTableResponse;
import com.enigma.wmb_api.model.response.PagingResponse;
import com.enigma.wmb_api.service.DinningTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<CommonResponse<List<DinningTableResponse>>> getAllTables(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        DinningTableRequest request = DinningTableRequest
                .builder()
                .id(id)
                .name(name)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .build();

        Page<DinningTableResponse> tablePage = service.findAll(request);

        PagingResponse paging = PagingResponse.builder()
                .totalPages(tablePage.getTotalPages())
                .totalElement(tablePage.getTotalElements())
                .page(tablePage.getPageable().getPageNumber())
                .size(tablePage.getPageable().getPageSize())
                .build();

        CommonResponse<List<DinningTableResponse>> response = CommonResponse.<List<DinningTableResponse>>
                builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(tablePage.getContent())
                .paging(paging)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create Dinning Table")
    @PostMapping
    public ResponseEntity<CommonResponse<DinningTableResponse>> create(@RequestBody DinningTableNewOrUpdateRequest dinningTable) {
        DinningTableResponse saved = service.create(dinningTable);
        CommonResponse<DinningTableResponse> response = CommonResponse.<DinningTableResponse>
                builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(saved)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);  
    }

    @Operation(summary = "Update Dinning Table")
    @PutMapping
    public ResponseEntity<CommonResponse<DinningTableResponse>> update(@RequestBody DinningTableNewOrUpdateRequest dinningTable) {
        DinningTableResponse updated = service.update(dinningTable);
        CommonResponse<DinningTableResponse> response = CommonResponse.<DinningTableResponse>
                builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(updated)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Delete Dinning Table")
    @DeleteMapping(value = "{id}/delete")
    public ResponseEntity<CommonResponse<?>> delete(@PathVariable String id) {
        service.delete(id);
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
