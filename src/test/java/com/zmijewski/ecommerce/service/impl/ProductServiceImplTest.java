package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.ProductDTO;
import com.zmijewski.ecommerce.dto.ShortProductDTO;
import com.zmijewski.ecommerce.enums.ProductSearchCriteria;
import com.zmijewski.ecommerce.enums.ProductSortType;
import com.zmijewski.ecommerce.exception.BrandNotFoundException;
import com.zmijewski.ecommerce.exception.CategoryNotFoundException;
import com.zmijewski.ecommerce.exception.NotEnoughProductException;
import com.zmijewski.ecommerce.exception.ProductNotFoundException;
import com.zmijewski.ecommerce.mapper.ProductMapper;
import com.zmijewski.ecommerce.model.Brand;
import com.zmijewski.ecommerce.model.Category;
import com.zmijewski.ecommerce.model.Image;
import com.zmijewski.ecommerce.model.Product;
import com.zmijewski.ecommerce.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @InjectMocks
    ProductServiceImpl productService;
    @Mock
    ProductSearchRepository productSearchRepository;
    @Mock
    ProductMapper productMapper;
    @Mock
    ImageRepository imageRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    BrandRepository brandRepository;
    @Mock
    ProductRepository productRepository;

    @Test
    void shouldFindAll() {
        //given
        int page = 0;
        int size = 10;
        ProductSortType sortType = ProductSortType.NAME_ASC;
        Page<Product> productPage
                = new PageImpl<>(Collections.singletonList(new Product()), PageRequest.of(page, size), 1L);
        when(productRepository.findAllAvailable(any())).thenReturn(productPage);
        when(productMapper.mapToShortProductDTO(any())).thenReturn(new ShortProductDTO());
        //when
        Page<ShortProductDTO> result = productService.findAll(page, size, sortType);
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldFindAllByCriteria() {
        //given
        int page = 0;
        int size = 10;
        ProductSortType sortType = ProductSortType.NAME_ASC;
        Map<ProductSearchCriteria, String> searchType = new HashMap<>();
        searchType.put(ProductSearchCriteria.BRAND, "Test");
        Page<Product> productPage
                = new PageImpl<>(Collections.singletonList(new Product()), PageRequest.of(page, size), 1L);
        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(productPage);
        when(productMapper.mapToShortProductDTO(any())).thenReturn(new ShortProductDTO());
        //when
        Page<ShortProductDTO> result = productService.findAllByCriteria(page, size, sortType, searchType);
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldFindAllBySearchWords() {
        //given
        int page = 0;
        int size = 10;
        ProductSortType sortType = ProductSortType.NAME_ASC;
        Page<Product> productPage
                = new PageImpl<>(Collections.singletonList(new Product()), PageRequest.of(page, size), 1L);
        when(productSearchRepository.searchForProduct(anyString(), anyInt(), anyInt(), any())).thenReturn(productPage);
        when(productMapper.mapToShortProductDTO(any())).thenReturn(new ShortProductDTO());
        //when
        Page<ShortProductDTO> result = productService.findAllBySearchWords(page, size, sortType, "TEST");
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldFindById() {
        //given
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        when(productMapper.mapToDTO(any())).thenReturn(new ProductDTO());
        //when
        ProductDTO result = productService.findById(1L);
        //then
        assertNotNull(result);
    }
    @Test
    void shouldNotFindById() {
        //given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));
    }
    @Test
    void shouldSaveProduct() {
        //given
        ProductDTO productToSave = new ProductDTO();
        productToSave.setBrandId(1L);
        productToSave.setCategoryId(1L);
        Product savedProduct = new Product();
        savedProduct.setId(1L);
        MultipartFile file = new MockMultipartFile("test", "test".getBytes());
        when(productMapper.mapToProduct(any())).thenReturn(new Product());
        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(new Brand()));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(new Category()));
        when(productRepository.save(any())).thenReturn(savedProduct);
        when(imageRepository.save(any())).thenReturn(new Image());
        //when
        Long result = productService.saveProduct(productToSave, new MultipartFile[]{file});
        //then
        assertEquals(1L, result);
    }
    @Test
    void shouldNotSaveIfBrandNotFound() {
        //given
        ProductDTO productToSave = new ProductDTO();
        productToSave.setBrandId(1L);
        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());
        MultipartFile file = new MockMultipartFile("test", "test".getBytes());
        //when && then
        assertThrows(BrandNotFoundException.class,
                () -> productService.saveProduct(productToSave, new MultipartFile[]{file}));
    }
    @Test
    void shouldNotSaveIfCategoryNotFound() {
        //given
        ProductDTO productToSave = new ProductDTO();
        productToSave.setBrandId(1L);
        productToSave.setCategoryId(1L);
        MultipartFile file = new MockMultipartFile("test", "test".getBytes());
        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(new Brand()));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(CategoryNotFoundException.class,
                () -> productService.saveProduct(productToSave, new MultipartFile[]{file}));
    }
    @Test
    void shouldUpdateIfFound() {
        //given
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        doNothing().when(productMapper).mapProductToUpdate(any(), any());
        when(productRepository.save(any())).thenReturn(new Product());
        //when
        productService.updateProduct(1L, new ProductDTO());
        //then
        verify(productRepository).findById(anyLong());
        verify(productMapper).mapProductToUpdate(any(), any());
        verify(productRepository).save(any());
    }
    @Test
    void shouldNotUpdateIfNotFound() {
        //given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, new ProductDTO()));
    }
    @Test
    void shouldDeleteIfFound() {
        //given
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        doNothing().when(productRepository).delete(any());
        //when
        productService.deleteProduct(1L);
        //then
        verify(productRepository).delete(any());
    }
    @Test
    void shouldNotDeleteIfNotFound() {
        //given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
    }
    @Test
    void shouldIncreaseProductQuantity() {
        //given
        doNothing().when(productRepository).increaseProductQuantity(anyLong(), anyInt());
        //when
        productService.increaseProductQuantity(10, 1L);
        //then
        verify(productRepository).increaseProductQuantity(anyLong(), anyInt());
    }
    @Test
    void shouldDecreaseProductQuantity()  {
        //given
        doNothing().when(productRepository).decreaseProductQuantity(anyLong(), anyInt());
        //when
        productService.decreaseProductQuantity(10, 1L);
        //then
        verify(productRepository).decreaseProductQuantity(anyLong(), anyInt());
    }
    @Test
    void shouldNotDecreaseProductQuantityIfQuantityIsLessThen0()  {
        //given
        doThrow(ConstraintViolationException.class).when(productRepository).decreaseProductQuantity(anyLong(), anyInt());
        //when && then
        assertThrows(NotEnoughProductException.class, () -> productService.decreaseProductQuantity(10, 1L));
    }

}