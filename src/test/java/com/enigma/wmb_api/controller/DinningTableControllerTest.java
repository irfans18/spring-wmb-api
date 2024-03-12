package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.model.request.DinningTableRequest;
import com.enigma.wmb_api.model.request.update.DinningTableNewOrUpdateRequest;
import com.enigma.wmb_api.model.response.CommonResponse;
import com.enigma.wmb_api.model.response.DinningTableResponse;
import com.enigma.wmb_api.model.response.UserResponse;
import com.enigma.wmb_api.service.DinningTableService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class DinningTableControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private DinningTableService service;

    @Test
    @WithMockUser(username = "sumanto", roles = {"ADMIN"})
    void getAllTables_WhenSuccess_ShouldReturnDinningTables() throws Exception {
        // Given
        DinningTableRequest request = DinningTableRequest
                .builder()
                .id("2c2fdf9e-d476-4933-883e-2bc55dcff110")
                .page(1)
                .size(10)
                .sortBy("name")
                .direction("asc")
                .build();

        List<DinningTableResponse> responseList = List.of(
                DinningTableResponse
                        .builder()
                        .id("2c2fdf9e-d476-4933-883e-2bc55dcff110")
                        .name("6F1R05q")
                        .build(),
                DinningTableResponse
                        .builder()
                        .id("2c2fdf9e-d476-4933-883e-2bc55dcff110")
                        .name("LpA3wXZ4")
                        .build(),
                DinningTableResponse
                        .builder()
                        .id("2c2fdf9e-d476-4933-883e-2bc55dcff110")
                        .name("WmpXFqlhRAn")
                        .build()
        );

        Page<DinningTableResponse> page = new Page<DinningTableResponse>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return responseList.size();
            }

            @Override
            public <U> Page<U> map(Function<? super DinningTableResponse, ? extends U> converter) {
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
                return responseList.size();
            }

            @Override
            public List<DinningTableResponse> getContent() {
                return responseList;
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
            public Iterator<DinningTableResponse> iterator() {
                return null;
            }
        };

        // When
        when(service.findAll(any())).thenReturn(page);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(APIUrl.DINNING_TABLE, request.getPathVariable()))
                .andExpect(status().isOk())
                .andDo(
                        result -> {
                            CommonResponse<List<DinningTableResponse>> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });

                            assertEquals(3, actual.getData().size());
                            assertEquals("6F1R05q", actual.getData().get(0).getName());
                            assertEquals("LpA3wXZ4", actual.getData().get(1).getName());
                            assertEquals("WmpXFqlhRAn", actual.getData().get(2).getName());

                        }
                )
                .andDo(print());
        verify(service, times(1)).findAll(any());
    }

    @Test
    @WithMockUser(username = "sumanto", roles = {"ADMIN"})
    void create_WhenSuccess_ShouldReturnDinningTable() throws Exception {
        // Given
        DinningTableNewOrUpdateRequest request = DinningTableNewOrUpdateRequest
                .builder()
                .name("6F1R05q")
                .build();

        DinningTableResponse response = DinningTableResponse
                .builder()
                .id("2c2fdf9e-d476-4933-883e-2bc55dcff110")
                .name("6F1R05q")
                .build();

        String payload = mapper.writeValueAsString(request);

        // Stubbing
        when(service.create(any())).thenReturn(response);

        // When - Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(APIUrl.DINNING_TABLE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andExpect(status().isCreated())
                .andDo(result -> {
                    CommonResponse<DinningTableResponse> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertAll(
                            "Create Dinning Table",
                            () -> assertEquals(HttpStatus.CREATED.value(), actual.getStatusCode()),
                            () -> assertEquals(ResponseMessage.SUCCESS_SAVE_DATA, actual.getMessage()),
                            () -> assertEquals(response.getId(), actual.getData().getId()),
                            () -> assertEquals(response.getName(), actual.getData().getName())
                    );
                }).andDo(print());

        verify(service, times(1)).create(any());

    }

    @Test
    @WithMockUser(username = "sumanto", roles = {"ADMIN"})
    void update_WhenSuccess_ShouldReturnDinningTable() throws Exception {
        // Given
        DinningTableNewOrUpdateRequest request = DinningTableNewOrUpdateRequest
                .builder()
                .id("2c2fdf9e-d476-4933-883e-2bc55dcff110")
                .name("6F1R05q")
                .build();

        DinningTableResponse response = DinningTableResponse
                .builder()
                .id("2c2fdf9e-d476-4933-883e-2bc55dcff110")
                .name("6F1R05q")
                .build();

        String payload = mapper.writeValueAsString(request);

        // Stubbing
        when(service.update(any())).thenReturn(response);

        // When - Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put(APIUrl.DINNING_TABLE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<DinningTableResponse> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertAll(
                            "Create Dinning Table",
                            () -> assertEquals(HttpStatus.OK.value(), actual.getStatusCode()),
                            () -> assertEquals(ResponseMessage.SUCCESS_UPDATE_DATA, actual.getMessage()),
                            () -> assertEquals(response.getId(), actual.getData().getId()),
                            () -> assertEquals(response.getName(), actual.getData().getName())
                    );
                }).andDo(print());

        verify(service, times(1)).update(any());
    }

    @Test
    @WithMockUser(username = "sumanto", roles = {"ADMIN"})
    void delete_WhenExist_ShouldDelete() throws Exception {
        // Given
        String id = "2c2fdf9e-d476-4933-883e-2bc55dcff110";

        // Stubbing
        doNothing().when(service).delete(id);


        // When - Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete(APIUrl.DINNING_TABLE + "/{id}/delete", id)
                )
                .andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<?> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertAll(
                            "Delete Dinning Table",
                            () -> assertEquals(HttpStatus.OK.value(), actual.getStatusCode()),
                            () -> assertEquals(ResponseMessage.SUCCESS_DELETE_DATA, actual.getMessage())
                    );
                }).andDo(print());

        // ArgumentCaptor
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        // verify(service, times(1)).delete(any());
        verify(service, times(1)).delete(argumentCaptor.capture());
        String capturedArgument = argumentCaptor.getValue();
        assertEquals(id, capturedArgument);
    }

}