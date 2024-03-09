package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.model.request.MenuRequest;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.model.response.PagingResponse;
import com.enigma.wmb_api.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "Menu", description = "Menu API")
@RequestMapping(APIUrl.MENUS)
public class MenuController {
    private final MenuService service;

    @Operation(summary = "Create Menu")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<Menu>> create(@RequestBody MenuRequest payload) {
        Menu menu = service.create(payload);
        CommonResponse<Menu> response = CommonResponse
                .<Menu>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(menu)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get All Menu")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<Menu>>> getAllMenus(
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

        Page<Menu> menuPage = service.findAll(request);

        PagingResponse paging = PagingResponse.builder()
                .totalPages(menuPage.getTotalPages())
                .totalElement(menuPage.getTotalElements())
                .page(menuPage.getPageable().getPageNumber())
                .size(menuPage.getPageable().getPageSize())
                .hasNext(menuPage.hasNext())
                .hasPrevious(menuPage.getPageable().hasPrevious())
                .build();

        CommonResponse<List<Menu>> response = CommonResponse
                .<List<Menu>>builder()
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(menuPage.getContent())
                .paging(paging)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update Menu")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<Menu>> update(@RequestBody MenuRequest payload) {
        Menu updated = service.update(payload);
        CommonResponse<Menu> response = CommonResponse
                .<Menu>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(updated)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete Menu")
    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> delete(@RequestBody MenuRequest payload) {
        service.delete(payload.getId());
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
