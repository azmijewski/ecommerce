package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.repository.ImageRepository;
import com.zmijewski.ecommerce.service.ImageService;
import org.springframework.stereotype.Service;


@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public void deleteImage(Long imageId) {
        imageRepository.findById(imageId)
                .ifPresent(imageRepository::delete);
    }
}
