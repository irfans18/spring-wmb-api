package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.entity.Image;
import com.enigma.wmb_api.repo.ImageRepo;
import com.enigma.wmb_api.service.ImageService;
import com.enigma.wmb_api.util.FileUtil;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepo repo;
    private final FileUtil fileUtil;


    @PostConstruct
    public void init() {
        if (!fileUtil.exists()) {
            try {
                fileUtil.createDirectory();
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @Override
    public Image create(MultipartFile file) {

        if (!List.of("image/png", "image/jpeg", "image/jpg", "image/svg+xml").contains(file.getContentType())) {
            throw new ConstraintViolationException(ResponseMessage.ERROR_INVALID_FILE_TYPE, null);
        }


        try {
            FileUtil.CreateResult result = fileUtil.create(file);

            Image image = Image
                    .builder()
                    .name(result.uniqueFileName())
                    .path(result.path().toString())
                    .size(file.getSize())
                    .contentType(file.getContentType())
                    .build();
            return repo.saveAndFlush(image);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Resource findOrFail(String id) {
        try {
            Image image = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));

            Path path = Paths.get(image.getPath());
            if (!fileUtil.exists(path)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
            }

            return new UrlResource(path.toUri());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            Image image = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
            Path path = Paths.get(image.getPath());
            if (!fileUtil.exists(path)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
            }
            fileUtil.delete(path);
            repo.deleteById(id);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
