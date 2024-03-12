package com.enigma.wmb_api.util;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Getter
public class FileUtil {
    private final Path directoryPath;

    public FileUtil(@Value("${wmb_api.multipart.path-location}") Path directoryPath) {
        this.directoryPath = directoryPath;
    }

    public boolean exists(Path path) {
        return Files.exists(path);
    }
    public boolean exists() {
        return Files.exists(directoryPath);
    }

    public void delete(Path path) throws IOException {
        Files.delete(path);
    }

    public CreateResult create(MultipartFile file) throws IOException {
        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = directoryPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), path);
        return new CreateResult(path, uniqueFileName);
    }

    @Builder
    public record CreateResult(Path path, String uniqueFileName) {}

    public Path createDirectory(Path path) throws IOException {
        return Files.createDirectory(path);
    }

    public void createDirectory() throws IOException {
        Files.createDirectory(directoryPath);
    }
}
