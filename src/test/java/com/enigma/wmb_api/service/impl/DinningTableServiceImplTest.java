package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.DinningTable;
import com.enigma.wmb_api.model.request.DinningTableRequest;
import com.enigma.wmb_api.model.request.update.DinningTableNewOrUpdateRequest;
import com.enigma.wmb_api.model.response.DinningTableResponse;
import com.enigma.wmb_api.repo.DinningTableRepo;
import com.enigma.wmb_api.service.DinningTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DinningTableServiceImplTest {
    @Mock
    private DinningTableRepo repo;
    private DinningTableService service;

    @BeforeEach
    void setUp() {
        service = new DinningTableServiceImpl(repo);
    }

    @Test
    void findOrFail_WhenExist_ShouldReturnDinningTable(TestReporter reporter) {
        // Given
        String id = "1";
        DinningTable dinningTable = DinningTable.builder()
                .id(id)
                .name("Table 1")
                .build();
        when(repo.findById(id)).thenReturn(Optional.of(dinningTable));

        // When
        DinningTable result = service.findOrFail(id);

        // Then
        assertEquals(dinningTable, result);
        verify(repo, times(1)).findById(id);
        reporter.publishEntry("findOrfail() - Positive Case - passed");
    }

    @Test
    void findOrFail_WhenNotExist_ShouldThrowResponseStatusException(TestReporter reporter) {
        // Given
        String id = null;

        // When
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.findOrFail(id);
        });

        // Then
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertThat(exception.getMessage()).contains("Dinning table not found");
        verify(repo, never()).findById(anyString());
        reporter.publishEntry("findOrfail() - Negative Case - passed");
    }

    @Test
    void create_WhenSuccess_ShouldReturnDinningTable(TestReporter reporter) {
        // Given
        DinningTableNewOrUpdateRequest request = DinningTableNewOrUpdateRequest.builder()
                .name("Table 1")
                .build();

        DinningTable dinningTable = DinningTable.builder()
                .name(request.getName())
                .build();

        // Stubbing
        when(repo.saveAndFlush(any())).thenReturn(dinningTable);

        // When
        DinningTableResponse result = service.create(request);

        // Then
        assertEquals(dinningTable.getId(), result.getId());
        assertEquals(dinningTable.getName(), result.getName());
        verify(repo, times(1)).saveAndFlush(any());
        reporter.publishEntry("create() - Positive Case - passed");
    }

    @Test
    void update_WhenSuccess_ShouldReturnDinningTable(TestReporter reporter) {
        // Given
        DinningTableNewOrUpdateRequest request = DinningTableNewOrUpdateRequest.builder()
                .id("1")
                .name("New Name")
                .build();
        DinningTable existingDinningTable = DinningTable.builder()
                .id("1")
                .name("Old Name")
                .build();
        when(repo.findById("1")).thenReturn(Optional.of(existingDinningTable));
        when(repo.saveAndFlush(any())).thenReturn(existingDinningTable);
        existingDinningTable.setName(request.getName());

        // When
        DinningTableResponse result = service.update(request);

        // Then
        assertEquals(existingDinningTable.getId(), result.getId());
        assertEquals(request.getName(), result.getName());
        verify(repo, times(1)).findById("1");
        verify(repo, times(1)).saveAndFlush(any());
        reporter.publishEntry("update() - Positive Case - passed");
    }


    @Test
    void update_WhenNotExist_ShouldThrowResponseStatusException() {
        // Given
        DinningTableNewOrUpdateRequest request = DinningTableNewOrUpdateRequest.builder()
                .id("1")
                .name("New Name")
                .build();
        when(repo.findById("1")).thenReturn(Optional.empty());

        // When
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.update(request));

        // Then
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(repo, times(1)).findById("1");
        verify(repo, never()).saveAndFlush(any());
    }


    @Test
    void findAll_WhenSuccess_ShouldReturnPage()  {
        // Given
        DinningTableRequest request = DinningTableRequest .builder()
                .id("1")
                .name("Table 1")
                .page(1)
                .size(10)
                .direction("ASC")
                .sortBy("name")
                .build();
        List<DinningTable> dinningTables = List.of(
                DinningTable.builder().id("1").name("Table 1").build(),
                DinningTable.builder().id("2").name("Table 2").build(),
                DinningTable.builder().id("3").name("Table 3").build()
        );
        Page<DinningTable> page = new PageImpl<>(dinningTables);

        // Stubbing
        when(repo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // When
        Page<DinningTableResponse> result = service.findAll(request);

        // Then
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getContent().size());
        assertEquals("1", result.getContent().get(0).getId());
        assertEquals("Table 1", result.getContent().get(0).getName());
        assertEquals("2", result.getContent().get(1).getId());
        assertEquals("Table 2", result.getContent().get(1).getName());
        assertEquals("3", result.getContent().get(2).getId());
        assertEquals("Table 3", result.getContent().get(2).getName());
        verify(repo, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }


    @Test
    void delete_WhenExist_ShouldDelete()  {
        // Given
        String id = "validId";
        DinningTable dinningTable = DinningTable.builder()
                .id(id)
                .name("Table 1")
                .build();
        when(repo.findById(id)).thenReturn(Optional.of(dinningTable));

        // When
        service.delete(id);

        // Then
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        verify(repo, times(1)).findById(idCaptor.capture());
        verify(repo, times(1)).deleteById(idCaptor.capture());
        assertEquals(id, idCaptor.getValue());
    }

    @Test
    void delete_WhenNotExist_ShouldThrowResponseStatusException()  {
        // Given
        String id = "invalidId";
        when(repo.findById(id)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResponseStatusException.class, () -> service.delete(id));

        // Then
        verify(repo, times(1)).findById(id);
        verify(repo, never()).deleteById(id);
    }
}