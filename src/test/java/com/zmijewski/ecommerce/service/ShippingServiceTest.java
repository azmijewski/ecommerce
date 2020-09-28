package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.ShippingDTO;
import com.zmijewski.ecommerce.exception.ShippingNotFoundException;
import com.zmijewski.ecommerce.mapper.ShippingMapper;
import com.zmijewski.ecommerce.model.entity.Shipping;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import com.zmijewski.ecommerce.repository.ShippingRepository;
import com.zmijewski.ecommerce.service.impl.ShippingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShippingServiceTest {
    @InjectMocks
    ShippingServiceImpl shippingService;
    @Mock
    ShippingRepository shippingRepository;
    @Mock
    ShippingMapper shippingMapper;

    @Test
    void shouldGetAll() {
        //given
        when(shippingRepository.findAll()).thenReturn(Collections.singletonList(new Shipping()));
        when(shippingMapper.mapToDTO(any())).thenReturn(new ShippingDTO());
        //when
        List<ShippingDTO> result = shippingService.getAll();
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldGetAllActive() {
        //given
        when(shippingRepository.findAllActiveByPaymentType(any())).thenReturn(Collections.singletonList(new Shipping()));
        when(shippingMapper.mapToDTO(any())).thenReturn(new ShippingDTO());
        //when
        List<ShippingDTO> result = shippingService.getAllActiveByType(PaymentType.POSTPAID);
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldGetById() {
        //given
        when(shippingRepository.findById(anyLong())).thenReturn(Optional.of(new Shipping()));
        when(shippingMapper.mapToDTO(any())).thenReturn(new ShippingDTO());
        //when
        ShippingDTO result = shippingService.getById(1L);
        //then
        assertNotNull(result);
    }
    @Test
    void shouldNotGetByIdIfNotExist() {
        //given
        when(shippingRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(ShippingNotFoundException.class, () -> shippingService.getById(1L));
    }
    @Test
    void shouldSaveShipping() {
        //given
        Long expectedId = 1L;
        Shipping savedShipping = new Shipping();
        savedShipping.setId(expectedId);
        when(shippingMapper.mapToShipping(any())).thenReturn(new Shipping());
        when(shippingRepository.save(any())).thenReturn(savedShipping);
        //when
        Long result = shippingService.saveShipping(new ShippingDTO());
        //then
        assertEquals(expectedId, result);
    }
    @Test
    void shouldEnable() {
        //given
        when(shippingRepository.findById(anyLong())).thenReturn(Optional.of(new Shipping()));
        when(shippingRepository.save(any())).thenReturn(new Shipping());
        //when
        shippingService.enableShipping(1L);
        //then
        verify(shippingRepository).save(any());
    }
    @Test
    void shouldNotEnableIfNotFound() {
        //given
        when(shippingRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(ShippingNotFoundException.class, () -> shippingService.enableShipping(1L));

    }
    @Test
    void shouldDisable() {
        //given
        when(shippingRepository.findById(anyLong())).thenReturn(Optional.of(new Shipping()));
        when(shippingRepository.save(any())).thenReturn(new Shipping());
        //when
        shippingService.disableShipping(1L);
        //then
        verify(shippingRepository).save(any());
    }
    @Test
    void shouldNotDisableIfNotFound() {
        //given
        when(shippingRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(ShippingNotFoundException.class, () -> shippingService.disableShipping(1L));

    }

}