package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.model.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void deleteImage(Long imageId);
}
