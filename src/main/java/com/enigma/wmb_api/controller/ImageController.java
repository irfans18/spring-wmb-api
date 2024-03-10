package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.APIUrl;
import com.enigma.wmb_api.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService service;

    // @PostMapping("/api/tes-upload")
    // public ResponseEntity<CommonResponse<Image>> uploadFile(@RequestPart("image") MultipartFile file) {
    //     Image image = service.create(file);
    //     CommonResponse<Image> response = CommonResponse.<Image>builder()
    //             .statusCode(HttpStatus.CREATED.value())
    //             .message("success")
    //             .data(image)
    //             .build();
    //     return ResponseEntity.accepted().body(response);
    // }

    @Operation(summary = "Get Menu Picture")
    @GetMapping(APIUrl.MENU_IMAGE_DOWNLOAD_API + "{id}")
    public ResponseEntity<Resource> getImage(@PathVariable("id") String id) {
        Resource image = service.findOrFail(id);
        String headerValue = String.format("attachment; filename=%s", image.getFilename());

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(image);
    }
}
