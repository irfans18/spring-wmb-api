package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.model.request.update.UserUpdateRequest;
import com.enigma.wmb_api.model.request.update.StatusUpdateRequest;
import com.enigma.wmb_api.model.request.UserRequest;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.model.response.PagingResponse;
import com.enigma.wmb_api.model.response.UserResponse;
import com.enigma.wmb_api.service.UserService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIUrl.USER)
@Tag(name = "User", description = "User API")
@SecurityRequirement(name = "Authorization")
public class UserController {
    private final UserService service;

    @Operation(summary = "Get All User")
    @PreAuthorize("hasAnyRole('ADMIN')")
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
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(userPage.getContent())
                .paging(paging)
                .build();


        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Update User")
    @PreAuthorize("hasAnyRole('ADMIN') || #request.id == @userCredentialServiceImpl.getByContext().getUser().getId()")
    @PutMapping(
            value = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<UserResponse>> update(
            @RequestBody UserUpdateRequest request
    ) {
        UserResponse user = service.update(request);
        CommonResponse<UserResponse> response = CommonResponse
                .<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update User Status By Id")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(
            value = "{id}/update",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<UserResponse>> updateStatusById(
            @PathVariable String id,
            @RequestParam Boolean status
    ) {
        UserResponse user = service.updateStatusById(new StatusUpdateRequest(id, status));
        CommonResponse<UserResponse> response = CommonResponse
                .<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }
}
