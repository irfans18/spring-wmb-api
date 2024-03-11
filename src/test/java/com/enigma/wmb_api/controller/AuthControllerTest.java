package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.model.request.AuthRequest;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.model.response.LoginResponse;
import com.enigma.wmb_api.model.response.RegisterResponse;
import com.enigma.wmb_api.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService service;
    @Autowired
    private ObjectMapper mapper;



    /* @Test
    @WithMockUser(username = "sumanto", roles = {"CUSTOMER"})
    void register_WhenSuccess_ShouldReturnResponse() {
        // Given
        AuthRequest request = AuthRequest.builder()
                .username("newUser")
                .password("password123")
                .build();
        RegisterResponse registered = RegisterResponse.builder()
                .username("newUser")
                .roles(List.of("ROLE_CUSTOMER"))
                .build();
        CommonResponse<RegisterResponse> expectedResponse = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(registered)
                .build();

        // Stubbing
        when(service.registerUser(any())).thenReturn(registered);

        AuthController controller = new AuthController(service);

        // When
        ResponseEntity<CommonResponse<RegisterResponse>> responseEntity = controller.register(request);

        // Then
        assertAll(
                "Register User",
                () -> assertEquals(expectedResponse.getData().getUsername(), responseEntity.getBody().getData().getUsername()),
                () -> assertEquals(expectedResponse.getData().getRoles(), responseEntity.getBody().getData().getRoles()),
                () -> assertEquals(expectedResponse.getStatusCode(), responseEntity.getStatusCode().value()),
                () -> assertEquals(expectedResponse.getMessage(), responseEntity.getBody().getMessage()),
                () -> assertEquals(expectedResponse.getData(), responseEntity.getBody().getData())
        );
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(service, times(1)).registerUser(any());
    } */

    @Test
    void register_WhenSuccess_ShouldReturnResponse() throws Exception {
        // Given
        AuthRequest payload = AuthRequest.builder()
                .username("newUser")
                .password("password123")
                .build();
        RegisterResponse registered = RegisterResponse.builder()
                .username("newUser")
                .roles(List.of("ROLE_CUSTOMER"))
                .build();

        String stringJson = mapper.writeValueAsString(payload);

        // Stubbing
        when(service.registerUser(any())).thenReturn(registered);

        // When
        mockMvc.perform(post(APIUrl.AUTH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringJson))
                .andExpect(status().isCreated())
                .andDo(result -> {
                    CommonResponse<RegisterResponse> response = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertAll(
                            "Register User",
                            () -> assertEquals(registered.getUsername(), response.getData().getUsername()),
                            () -> assertEquals(registered.getRoles(), response.getData().getRoles()),
                            () -> assertEquals(HttpStatus.CREATED.value(), response.getStatusCode()),
                            () -> assertEquals(ResponseMessage.SUCCESS_SAVE_DATA, response.getMessage()),
                            () -> assertEquals(registered.getUsername(), response.getData().getUsername())
                    );
                }).andDo(print());

    }


    @Test
    void login_WhenSuccess_ShouldReturnResponse() throws Exception {

        // Given
        AuthRequest request = AuthRequest.builder()
                .username("sumanto")
                .password("password123")
                .build();
        LoginResponse loginResponse = LoginResponse.builder()
                .username("sumanto")
                .roles(List.of("ROLE_CUSTOMER"))
                .build();

        String payload = mapper.writeValueAsString(request);

        // Stubbing
        when(service.login(any())).thenReturn(loginResponse);

        // When
        mockMvc.perform(
                        post(APIUrl.AUTH + "/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                ).andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<LoginResponse> response = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    assertAll(
                            "Login",
                            () -> assertEquals(loginResponse.getUsername(), response.getData().getUsername()),
                            () -> assertEquals(loginResponse.getRoles(), response.getData().getRoles()),
                            () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode()),
                            () -> assertEquals(ResponseMessage.SUCCESS_LOGIN, response.getMessage()),
                            () -> assertEquals(loginResponse.getUsername(), response.getData().getUsername())
                    );

                }).andDo(print());

    }
}