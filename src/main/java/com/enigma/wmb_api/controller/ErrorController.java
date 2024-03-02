package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.model.response.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ErrorController {
    /**
     * ResponseEntity<?> : ? -> berfungsi untuk object generic mirip seperti Object class
     * */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CommonResponse<?>> handleResponseStatusException(ResponseStatusException e) {
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(e.getStatusCode().value())
                .message(e.getReason())
                .build();
        return ResponseEntity
                .status(e.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonResponse<?>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        CommonResponse.CommonResponseBuilder<Object> builder = CommonResponse.<Object>builder();
        HttpStatus httpStatus;
        if (e.getMessage().contains("foreign key constraint ")) {
            builder.statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("failed to delete data because it is used in another table");
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (e.getMessage().contains("unique constraint")) {
            builder.statusCode(HttpStatus.CONFLICT.value())
                    .message("failed to insert data because it already exists");
            httpStatus = HttpStatus.CONFLICT;
        }else {
            builder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal Server Error");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        }

        return ResponseEntity
                .status(httpStatus)
                .body(builder.build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<?>> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(errors.toString())
                .build();


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
