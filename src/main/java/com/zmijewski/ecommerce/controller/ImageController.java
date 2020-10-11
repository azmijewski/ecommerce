package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.service.ImageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
@Log4j2
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @DeleteMapping("images/{imageId}")
    public ResponseEntity<?> deleteImageById(@PathVariable(name = "imageId") Long imageId) {
        log.info("Deleting image with id: {}", imageId);
        imageService.deleteImage(imageId);
        log.info("Image deleted successfully");
        return ResponseEntity.noContent().build();
    }
}
