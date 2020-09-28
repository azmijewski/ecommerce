package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.model.entity.Image;
import com.zmijewski.ecommerce.repository.ImageRepository;
import com.zmijewski.ecommerce.service.impl.ImageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
    @InjectMocks
    ImageServiceImpl imageService;
    @Mock
    ImageRepository imageRepository;

    @Test
    void shouldDeleteImage() {
        //given
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(new Image()));
        doNothing().when(imageRepository).delete(any());
        //when
        imageService.deleteImage(1L);
        //then
        verify(imageRepository).delete(any());
    }
    @Test
    void shouldNotDeleteImageIfNotFound() {
        //given
        when(imageRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        imageService.deleteImage(1L);
        //then
        verifyNoMoreInteractions(imageRepository);
    }

}