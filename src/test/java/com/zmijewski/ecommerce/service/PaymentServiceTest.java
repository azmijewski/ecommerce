package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.PaymentDTO;
import com.zmijewski.ecommerce.exception.PaymentNotFoundException;
import com.zmijewski.ecommerce.mapper.PaymentMapper;
import com.zmijewski.ecommerce.model.entity.Payment;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import com.zmijewski.ecommerce.repository.PaymentRepository;
import com.zmijewski.ecommerce.service.impl.PaymentServiceImpl;
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
class PaymentServiceTest {
    @InjectMocks
    PaymentServiceImpl paymentService;
    @Mock
    PaymentRepository paymentRepository;
    @Mock
    PaymentMapper paymentMapper;

    @Test
    void shouldGetAll() {
        //given
        when(paymentRepository.findAll()).thenReturn(Collections.singletonList(new Payment()));
        when(paymentMapper.mapToDTO(any())).thenReturn(new PaymentDTO());
        //when
        List<PaymentDTO> result = paymentService.getAll();
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldGetAllActive() {
        //given
        when(paymentRepository.findAllActiveByType(any())).thenReturn(Collections.singletonList(new Payment()));
        when(paymentMapper.mapToDTO(any())).thenReturn(new PaymentDTO());
        //when
        List<PaymentDTO> result = paymentService.getAllActiveByType(PaymentType.POSTPAID);
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldGetById() {
        //given
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(new Payment()));
        when(paymentMapper.mapToDTO(any())).thenReturn(new PaymentDTO());
        //when
        PaymentDTO result = paymentService.getById(1L);
        //then
        assertNotNull(result);
    }
    @Test
    void shouldNotGetByIdIfNotExist() {
        //given
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(PaymentNotFoundException.class, () -> paymentService.getById(1L));
    }
    @Test
    void shouldSavePayment() {
        //given
        Long expectedId = 1L;
        Payment savedPayment = new Payment();
        savedPayment.setId(expectedId);
        when(paymentMapper.mapToPayment(any())).thenReturn(new Payment());
        when(paymentRepository.save(any())).thenReturn(savedPayment);
        //when
        Long result = paymentService.savePayment(new PaymentDTO());
        //then
        assertEquals(expectedId, result);
    }
    @Test
    void shouldEnable() {
        //given
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(new Payment()));
        when(paymentRepository.save(any())).thenReturn(new Payment());
        //when
        paymentService.enablePayment(1L);
        //then
        verify(paymentRepository).save(any());
    }
    @Test
    void shouldNotEnableIfNotFound() {
        //given
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(PaymentNotFoundException.class, () -> paymentService.enablePayment(1L));

    }
    @Test
    void shouldDisable() {
        //given
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(new Payment()));
        when(paymentRepository.save(any())).thenReturn(new Payment());
        //when
        paymentService.disablePayment(1L);
        //then
        verify(paymentRepository).save(any());
    }
    @Test
    void shouldNotDisableIfNotFound() {
        //given
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(PaymentNotFoundException.class, () -> paymentService.disablePayment(1L));

    }

}