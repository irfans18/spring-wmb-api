package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.model.request.UserRequest;
import com.enigma.wmb_api.model.request.update.UserUpdateRequest;
import com.enigma.wmb_api.model.request.update.StatusUpdateRequest;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.model.response.UserResponse;
import com.enigma.wmb_api.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    UserService service;


    @Test
    @WithMockUser(username = "sumanto", roles = {"ADMIN"})
    void getAllUser_WhenSuccess_ShouldReturnResponse() throws Exception {
        // Given
        UserRequest request = UserRequest
                .builder()
                .name("name")
                .phoneNumber("phoneNumber")
                .status(true)
                .page(1)
                .size(10)
                .sortBy("name")
                .direction("asc")
                .build();

        List<UserResponse> userResponses = List.of(
                UserResponse.builder().id("1").name("name").phoneNumber("phoneNumber").build(),
                UserResponse.builder().id("2").name("name").phoneNumber("phoneNumber").build(),
                UserResponse.builder().id("3").name("name").phoneNumber("phoneNumber").build()
        );

        Page<UserResponse> userResponsePage = new Page<UserResponse>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return userResponses.size();
            }

            @Override
            public <U> Page<U> map(Function<? super UserResponse, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return request.getPage() - 1;
            }

            @Override
            public int getSize() {
                return request.getSize();
            }

            @Override
            public int getNumberOfElements() {
                return userResponses.size();
            }

            @Override
            public List<UserResponse> getContent() {
                return userResponses;
            }

            @Override
            public boolean hasContent() {
                return true;
            }

            @Override
            public Sort getSort() {
                return Sort.by(request.getSortBy());
            }

            @Override
            public boolean isFirst() {
                return true;
            }

            @Override
            public boolean isLast() {
                return true;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<UserResponse> iterator() {
                return null;
            }
        };

        // When
        when(service.findAll(any())).thenReturn(userResponsePage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(APIUrl.USER + request.getPathVariable()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<List<UserResponse>> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertAll(
                            "Get All User",
                            () -> assertEquals(HttpStatus.OK.value(), actual.getStatusCode()),
                            () -> assertEquals(ResponseMessage.SUCCESS_GET_DATA, actual.getMessage()),
                            () -> assertEquals(userResponses.size(), actual.getData().size())
                    );
                });
        verify(service, times(1)).findAll(any());
    }


    @Test
    @WithMockUser(username = "sumanto", roles = {"ADMIN"})
    void update_WhenSuccess_ShouldReturnResponse() throws Exception {
        // Given
        UserUpdateRequest request = UserUpdateRequest
                .builder()
                .id("1")
                .name("name")
                .phoneNumber("phoneNumber")
                .build();
        UserResponse response = UserResponse.builder()
                .id("1")
                .name("name")
                .phoneNumber("phoneNumber")
                .build();
        String payload = mapper.writeValueAsString(request);
        // Stubbing
        when(service.update(any())).thenReturn(response);

        // When - Then
        mockMvc.perform(
                        MockMvcRequestBuilders.put(APIUrl.USER + "/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<UserResponse> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertAll(
                            "Update User",
                            () -> assertEquals(HttpStatus.OK.value(), actual.getStatusCode()),
                            () -> assertEquals(ResponseMessage.SUCCESS_UPDATE_DATA, actual.getMessage()),
                            () -> assertEquals(response.getId(), actual.getData().getId())
                    );
                });
        verify(service, times(1)).update(any());
    }

    @Test
    @WithMockUser(username = "sumanto", roles = {"ADMIN"})
    void updateStatusById() throws Exception {
        // Given
        StatusUpdateRequest request = StatusUpdateRequest.builder()
                .userId("1")
                .status(false)
                .build();
        UserResponse response = UserResponse.builder()
                .id(request.getUserId())
                .name("name")
                .phoneNumber("phoneNumber")
                .status(request.getStatus())
                .build();

        // Stubbing
        when(service.updateStatusById(any())).thenReturn(response);


        // When - Then
        mockMvc.perform(
                        MockMvcRequestBuilders.put(APIUrl.USER + "/{id}/update?status={status}", request.getUserId(), request.getStatus())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<UserResponse> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertAll(
                            "Update User Status By Id",
                            () -> assertEquals(HttpStatus.OK.value(), actual.getStatusCode()),
                            () -> assertEquals(ResponseMessage.SUCCESS_UPDATE_DATA, actual.getMessage()),
                            () -> assertEquals(response.getId(), actual.getData().getId())
                    );
                });
        verify(service, times(1)).updateStatusById(any());
    }
}