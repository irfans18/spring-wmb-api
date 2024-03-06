package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.model.request.MenuRequest;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.model.response.PagingResponse;
import com.enigma.wmb_api.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIUrl.MENUS)
public class MenuController {
    private final MenuService service;

    @GetMapping()
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
}
