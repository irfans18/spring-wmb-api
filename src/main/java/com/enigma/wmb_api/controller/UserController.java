package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.entity.User;
import com.enigma.wmb_api.model.request.UpdateStatusRequest;
import com.enigma.wmb_api.model.request.UserRequest;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.model.response.PagingResponse;
import com.enigma.wmb_api.model.response.UserResponse;
import com.enigma.wmb_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIUrl.USER)
public class UserController {
    private final UserService service;

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<CommonResponse<List<UserResponse>>> getAllUser(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Boolean status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        UserRequest request = UserRequest
                .builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .status(status)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .build();
        Page<UserResponse> userPage = service.findAll(request);

        PagingResponse paging = PagingResponse.builder()
                .totalPages(userPage.getTotalPages())
                .totalElement(userPage.getTotalElements())
                .page(userPage.getPageable().getPageNumber())
                .size(userPage.getPageable().getPageSize())
                .hasNext(userPage.hasNext())
                .hasPrevious(userPage.getPageable().hasPrevious())
                .build();

        CommonResponse<List<UserResponse>> response = CommonResponse
                .<List<UserResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all user successfully")
                .data(userPage.getContent())
                .paging(paging)
                .build();


        return ResponseEntity.ok(response);
    }
    @PutMapping(
            value = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<UserResponse>> update(@RequestBody UserRequest request) {
        UserResponse user = service.update(request);
        CommonResponse<UserResponse> response = CommonResponse
                .<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Update user successfully")
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping(
            value = "{id}/update",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<UserResponse>> updateStatusById(
            @PathVariable String id,
            @RequestParam(required = false) Boolean status
    ) {
        UserResponse user = service.updateStatusById(new UpdateStatusRequest(id, status));
        CommonResponse<UserResponse> response = CommonResponse
                .<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Update user status successfully")
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }
}
