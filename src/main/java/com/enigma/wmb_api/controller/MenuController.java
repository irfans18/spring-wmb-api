package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.model.request.MenuRequest;
import com.enigma.wmb_api.model.request.update.MenuNewOrUpdateRequest;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.model.response.MenuResponse;
import com.enigma.wmb_api.model.response.PagingResponse;
import com.enigma.wmb_api.service.MenuService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "Menu", description = "Menu API")
@RequestMapping(APIUrl.MENUS)
public class MenuController {
    private final MenuService service;
    private final ObjectMapper mapper;

    @Operation(summary = "Create Menu")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<MenuResponse>> create(
            @RequestPart(required = false) MultipartFile image,
            @RequestPart(name = "menu") String payload
    ) {
        CommonResponse.CommonResponseBuilder<MenuResponse> responseBuilder = CommonResponse.<MenuResponse>builder();

        try {
            MenuNewOrUpdateRequest request = mapper.readValue(payload, new TypeReference<>() {});
            request.setImage(image != null ? image : null);
            MenuResponse menuResponse = service.create(request);
            responseBuilder.statusCode(HttpStatus.CREATED.value());
            responseBuilder.message(ResponseMessage.SUCCESS_SAVE_DATA);
            responseBuilder.data(menuResponse);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(responseBuilder.build());
        } catch (Exception e) {
            responseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseBuilder.message(ResponseMessage.ERROR_INTERNAL_SERVER);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBuilder.build());
        }
    }

    @Operation(summary = "Get All Menu")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<MenuResponse>>> getAllMenus(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) Integer price
    ) {
        MenuRequest request = MenuRequest
                .builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(name)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .price(price)
                .build();

        Page<MenuResponse> menuPage = service.findAll(request);

        PagingResponse paging = PagingResponse.builder()
                .totalPages(menuPage.getTotalPages())
                .totalElement(menuPage.getTotalElements())
                .page(menuPage.getPageable().getPageNumber())
                .size(menuPage.getPageable().getPageSize())
                .hasNext(menuPage.hasNext())
                .hasPrevious(menuPage.getPageable().hasPrevious())
                .build();

        CommonResponse<List<MenuResponse>> response = CommonResponse
                .<List<MenuResponse>>builder()
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(menuPage.getContent())
                .paging(paging)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update Menu")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<MenuResponse>> update(
            @RequestPart(required = false) MultipartFile image,
            @RequestPart(name = "menu") String payload
    ) {
        CommonResponse.CommonResponseBuilder<MenuResponse> responseBuilder = CommonResponse.<MenuResponse>builder();

        try {
            MenuNewOrUpdateRequest request = mapper.readValue(payload, new TypeReference<>() {});
            request.setImage(image != null ? image : null);
            MenuResponse menuResponse = service.update(request);
            responseBuilder.statusCode(HttpStatus.CREATED.value());
            responseBuilder.message(ResponseMessage.SUCCESS_SAVE_DATA);
            responseBuilder.data(menuResponse);

            return ResponseEntity
                    .ok(responseBuilder.build());
        } catch (Exception e) {
            responseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseBuilder.message(ResponseMessage.ERROR_INTERNAL_SERVER);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBuilder.build());
        }
    }

    @Operation(summary = "Delete Menu By Id")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(
            value = "/{id}/delete",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> delete(@PathVariable String id){
        service.delete(id);
        CommonResponse<?> response = CommonResponse
                .<Object>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


}
