package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.BrandDTO;
import com.zmijewski.ecommerce.enums.BrandSortType;
import com.zmijewski.ecommerce.exception.BrandNotFoundException;
import com.zmijewski.ecommerce.mapper.BrandMapper;
import com.zmijewski.ecommerce.model.Brand;
import com.zmijewski.ecommerce.repository.BrandRepository;
import com.zmijewski.ecommerce.service.impl.BrandServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {
    @InjectMocks
    BrandServiceImpl brandService;
    @Mock
    BrandRepository brandRepository;
    @Mock
    BrandMapper brandMapper;

    @Test
    void shouldFindAll() {
        //given
        when(brandRepository.findAll(any(Sort.class))).thenReturn(Collections.singletonList(new Brand()));
        when(brandMapper.mapToDTO(any())).thenReturn(new BrandDTO());
        //when
        List<BrandDTO> result = brandService.findAll(BrandSortType.ID_ASC);
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldFindById() {
        //given
        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(new Brand()));
        when(brandMapper.mapToDTO(any())).thenReturn(new BrandDTO());
        //when
        BrandDTO result = brandService.findById(1L);
        //then
        assertNotNull(result);
    }
    @Test
    void shouldNotFindByIdIfNotExist() {
        //given
        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(BrandNotFoundException.class, () -> brandService.findById(1L));
    }
    @Test
    void shouldSaveBrand() {
        //given
        Long expectedId = 1L;
        Brand savedBrand = Brand.builder()
                .id(expectedId)
                .build();
        when(brandMapper.mapToBrand(any())).thenReturn(new Brand());
        when(brandRepository.save(any())).thenReturn(savedBrand);
        //when
        Long result = brandService.saveBrand(new BrandDTO());
        //then
        assertEquals(expectedId, result);
    }
    @Test
    void shouldModifyBrand() {
        //given
        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(new Brand()));
        doNothing().when(brandMapper).mapDataToUpdate(any(), any());
        when(brandRepository.save(any())).thenReturn(new Brand());
        //when
        brandService.modifyBrand(1L, new BrandDTO());
        //then
        verify(brandRepository).save(any());
    }
    @Test
    void shouldNotModifyBrandIfNotExist() {
        //given
        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(BrandNotFoundException.class, () -> brandService.modifyBrand(1L, new BrandDTO()));
    }

}