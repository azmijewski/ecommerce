package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.PaymentDTO;
import com.zmijewski.ecommerce.model.entity.Payment;
import com.zmijewski.ecommerce.model.enums.PaymentType;

import java.util.List;

public interface PaymentService {
    List<PaymentDTO> getAll();
    List<PaymentDTO> getAllActiveByType(PaymentType paymentType);
    PaymentDTO getById(Long paymentId);
    Long savePayment(PaymentDTO paymentDTO);
    void enablePayment(Long paymentId);
    void disablePayment(Long paymentId);
}
