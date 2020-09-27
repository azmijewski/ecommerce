package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.PaymentDTO;
import com.zmijewski.ecommerce.exception.PaymentNotFoundException;
import com.zmijewski.ecommerce.mapper.PaymentMapper;
import com.zmijewski.ecommerce.model.entity.Payment;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import com.zmijewski.ecommerce.repository.PaymentRepository;
import com.zmijewski.ecommerce.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public List<PaymentDTO> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getAllActiveByType(PaymentType paymentType) {
        return paymentRepository.findAllActiveByType(paymentType)
                .stream()
                .map(paymentMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO getById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Could not find payment with id: " + paymentId));
        return paymentMapper.mapToDTO(payment);
    }

    @Override
    public Long savePayment(PaymentDTO paymentDTO) {
        Payment paymentToSave = paymentMapper.mapToPayment(paymentDTO);
        return paymentRepository.save(paymentToSave).getId();
    }

    @Override
    public void enablePayment(Long paymentId) {
        changeAvailability(paymentId, true);
    }

    @Override
    public void disablePayment(Long paymentId) {
        changeAvailability(paymentId, false);
    }

    private void changeAvailability(Long paymentId, boolean status) {
        Payment paymentToChangeAvailability = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Could not find payment with id: " + paymentId));
        paymentToChangeAvailability.setIsAvailable(status);
        paymentRepository.save(paymentToChangeAvailability);
    }
}
