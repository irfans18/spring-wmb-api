package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.Image;
import com.enigma.wmb_api.repo.ImageRepo;
import com.enigma.wmb_api.service.ImageService;
import com.enigma.wmb_api.util.FileUtil;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @TempDir
    Path tempDir;
    @Mock
    private FileUtil fileUtil;
    @Mock
    private ImageRepo repo;
    private ImageService service;

    @BeforeEach
    void setUp() {
        service = new ImageServiceImpl(repo, fileUtil);
    }

    @Test
    void create_WhenValidFileType_ShouldCreateAndReturnImage() throws IOException {
        // Given
        MultipartFile file = new MockMultipartFile("test.png", "test.png", "image/png", "test".getBytes());
        Image image = Image.builder()
                .id("test")
                .name("test.png")
                .path(tempDir.resolve(file.getName()).toString())
                .size(4L)
                .contentType("image/png")
                .build();
        FileUtil.CreateResult createResult = FileUtil.CreateResult.builder()
                .path(tempDir.resolve(file.getName()))
                .uniqueFileName(file.getName())
                .build();

        // Stubbing
        when(repo.saveAndFlush(any())).thenReturn(image);
        when(fileUtil.create(file)).thenReturn(createResult);

        // When
        Image result = service.create(file);

        // Then
        assertAll(
                "Should return an image",
                () -> assertNotNull(result),
                () -> assertEquals(file.getOriginalFilename(), result.getName()),
                () -> assertEquals(file.getSize(), result.getSize()),
                () -> assertEquals(file.getContentType(), result.getContentType())
        );

        verify(repo, times(1)).saveAndFlush(any());
        verify(fileUtil, times(1)).create(file);
    }

    @Test
    void create_WhenIOException_ShouldThrowResponseStatusException() throws IOException {
        // Given
        MultipartFile file = new MockMultipartFile("test.png", "test.png", "image/png", "test".getBytes());

        // Stubbing
        when(fileUtil.create(file)).thenThrow(IOException.class);

        // When & Then
        assertThrows(ResponseStatusException.class,
                () -> service.create(file)
        );
    }

    @Test
    void create_WhenInvalidFileType_ShouldThrowConstraintViolationException() {
        // Given
        MultipartFile file = new MockMultipartFile("test.txt", "test.png", "text/plain", "test".getBytes());

        // When & Then
        assertThrows(ConstraintViolationException.class, () -> service.create(file));
    }

    @Test
    void findOrFail_WhenExist_ShouldReturnResource() {
        // Given
        Image image = Image.builder()
                .id("test")
                .name("test.png")
                .path(tempDir.resolve("test.png").toString())
                .size(4L)
                .contentType("image/png")
                .build();

        // Stubbing
        when(repo.findById(any())).thenReturn(Optional.of(image));
        when(fileUtil.exists(any(Path.class))).thenReturn(true);

        // When
        Resource result = service.findOrFail(image.getId());

        // Then
        assertAll(
                "Should return a resource",
                () -> assertNotNull(result),
                () -> assertEquals(image.getName(), result.getFilename())
        );

        assertAll(
                "Verify all method invoke",
                () -> verify(repo, times(1)).findById(any()),
                () -> verify(fileUtil, times(1)).exists(any(Path.class))
        );

    }

    @Test
    void findOrFail_WhenFileNotExist_ShouldThrowResponseStatusException() {
        // Given
        Image image = Image.builder()
                .id("test")
                .name("test.png")
                .path(tempDir.resolve("test.png").toString())
                .size(4L)
                .contentType("image/png")
                .build();

        // Stubbing
        when(repo.findById(any())).thenReturn(Optional.of(image));
        when(fileUtil.exists(any(Path.class))).thenReturn(false);

        // When
        assertThrows(ResponseStatusException.class,
                () -> service.findOrFail(image.getId())
        );

        // Then
        assertAll(
                "Verify all method invoke",
                () -> verify(repo, times(1)).findById(any()),
                () -> verify(fileUtil, times(1)).exists(any(Path.class))
        );
    }

    @Test
    void findOrFail_WhenFileNotExist_ShouldThrowResponseStatusExceptionWithMessage() {
        // Given
        Image image = Image.builder()
                .id("test")
                .name("test.png")
                .path(tempDir.resolve("test.png").toString())
                .size(4L)
                .contentType("image/png")
                .build();

        // Stubbing
        when(repo.findById(any())).thenReturn(Optional.of(image));
        when(fileUtil.exists(any(Path.class))).thenReturn(false);

        // When
        assertThrows(ResponseStatusException.class,
                () -> service.findOrFail(image.getId())
        );
    }

    @Test
    void deleteById_WhenExist_ShouldDelete() throws IOException {
        // Given
        Image image = Image.builder()
                .id("test")
                .name("test.png")
                .path(tempDir.resolve("test.png").toString())
                .size(4L)
                .contentType("image/png")
                .build();

        // Stubbing
        when(repo.findById(any())).thenReturn(Optional.of(image));
        when(fileUtil.exists(any(Path.class))).thenReturn(true);
        doNothing().when(fileUtil).delete(any());
        doNothing().when(repo).deleteById(any());

        // When
        service.deleteById(image.getId());

        // Then
        assertAll(
                "Should delete the image",
                () -> verify(fileUtil, times(1)).exists(any(Path.class)),
                () -> verify(fileUtil, times(1)).delete(any(Path.class)),
                () -> verify(repo, times(1)).findById(any()),
                () -> verify(repo, times(1)).deleteById(image.getId())
        );
    }

    @Test
    void deleteById_WhenFileNotExist_ShouldThrowResponseStatusException() throws IOException {
        // Given
        Image image = Image.builder()
                .id("test")
                .name("test.png")
                .path(tempDir.resolve("test.png").toString())
                .size(4L)
                .contentType("image/png")
                .build();

        // Stubbing
        when(repo.findById(any())).thenReturn(Optional.of(image));
        when(fileUtil.exists(any(Path.class))).thenReturn(false);

        // When - Then
        assertThrows(ResponseStatusException.class,
                () -> service.deleteById(image.getId())
        );
    }
}
