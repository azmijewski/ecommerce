package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.CategoryDTO;
import com.zmijewski.ecommerce.exception.CategoryCannotBeRemovedException;
import com.zmijewski.ecommerce.exception.CategoryNameAlreadyExistException;
import com.zmijewski.ecommerce.exception.CategoryNotFoundException;
import com.zmijewski.ecommerce.mapper.CategoryMapper;
import com.zmijewski.ecommerce.model.entity.Category;
import com.zmijewski.ecommerce.model.entity.Image;
import com.zmijewski.ecommerce.repository.CategoryRepository;
import com.zmijewski.ecommerce.repository.ImageRepository;
import com.zmijewski.ecommerce.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @InjectMocks
    CategoryServiceImpl categoryService;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    CategoryMapper categoryMapper;
    @Mock
    ImageRepository imageRepository;

    @Test
    void shouldGetAll() {
        //given
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(new Category()));
        when(categoryMapper.mapToDTO(any())).thenReturn(new CategoryDTO());
        //when
        List<CategoryDTO> result = categoryService.getAll();
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldGetById() {
        //given
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(new Category()));
        when(categoryMapper.mapToDTO(any())).thenReturn(new CategoryDTO());
        //when
        CategoryDTO result = categoryService.getById(1L);
        //then
        assertNotNull(result);
    }
    @Test
    void shouldNotGetByIdIfNotFound() {
        //given
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getById(1L));
    }
    @Test
    void shouldSaveCategory() {
        //given
        MultipartFile file = new MockMultipartFile("test", "test", "test", "test".getBytes());
        Long expectedId = 1L;
        Category savedCategory = new Category();
        savedCategory.setId(expectedId);
        when(categoryMapper.mapToCategory(any())).thenReturn(new Category());
        when(imageRepository.save(any())).thenReturn(new Image());
        when(categoryRepository.save(any())).thenReturn(savedCategory);
        //when
        Long result = categoryService.saveCategory(new CategoryDTO(), file);
        //then
        assertEquals(expectedId, result);
    }
    @Test
    void shouldNotSaveCategoryIfNameAlreadyExist() {
        //given
        MultipartFile file = new MockMultipartFile("test", "test", "test", "test".getBytes());
        when(imageRepository.save(any())).thenReturn(new Image());
        when(categoryMapper.mapToCategory(any())).thenReturn(new Category());
        when(categoryRepository.save(any())).thenThrow(ConstraintViolationException.class);
        //when && then
        assertThrows(CategoryNameAlreadyExistException.class, () -> categoryService.saveCategory(new CategoryDTO(), file));
    }

    @Test
    void shouldModifyCategory() {
        //given
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(new Category()));
        doNothing().when(categoryMapper).mapDataToUpdate(any(), any());
        when(categoryRepository.save(any())).thenReturn(new Category());
        //when
        categoryService.modifyCategory(1L, new CategoryDTO());
        //then
        verify(categoryRepository).save(any());
    }
    @Test
    void shouldNotModifyIfNotFound() {
        //given
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.modifyCategory(1L, new CategoryDTO()));
    }
    @Test
    void shouldNotModifyIfNameAlreadyExist() {
        //given
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(new Category()));
        doNothing().when(categoryMapper).mapDataToUpdate(any(), any());
        when(categoryRepository.save(any())).thenThrow(ConstraintViolationException.class);
        //when && then
        assertThrows(CategoryNameAlreadyExistException.class, () -> categoryService.modifyCategory(1L, new CategoryDTO()));
    }
    @Test
    void shouldDelete() {
        //given
        when(categoryRepository.hasAnyNoProduct(anyLong())).thenReturn(false);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(new Category()));
        doNothing().when(categoryRepository).delete(any());
        //when
        categoryService.deleteCategory(1L);
        //then
        verify(categoryRepository).delete(any());
    }
    @Test
    void shouldNotDeleteIfHasProducts() {
        //given
        when(categoryRepository.hasAnyNoProduct(1L)).thenReturn(true);
        //when && then
        assertThrows(CategoryCannotBeRemovedException.class, () -> categoryService.deleteCategory(1L));
    }
    @Test
    void shouldNotDeleteCategoryIfNotFound() {
        //given
        when(categoryRepository.hasAnyNoProduct(anyLong())).thenReturn(false);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }


}