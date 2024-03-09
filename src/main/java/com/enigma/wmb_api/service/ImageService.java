package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image create(MultipartFile file);

    Resource findOrFail(String id);

    void deleteById(String id);
}
