package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.AuditDTO;
import com.zmijewski.ecommerce.dto.PaymentDTO;
import com.zmijewski.ecommerce.exception.PaymentNotFoundException;
import com.zmijewski.ecommerce.mapper.PaymentMapper;
import com.zmijewski.ecommerce.model.entity.Payment;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import com.zmijewski.ecommerce.repository.PaymentRepository;
import com.zmijewski.ecommerce.service.PaymentService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String PAYMENT_ADDED_MESSAGE = "Added new payment: ";
    private static final String PAYMENT_ENABLED_MESSAGE = "Enabled payment: ";
    private static final String PAYMENT_DISABLED_MESSAGE = "Disabled payment: ";

    private static final String AUDIT_QUEUE = "auditQueue";

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final RabbitTemplate rabbitTemplate;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              PaymentMapper paymentMapper,
                              RabbitTemplate rabbitTemplate) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.rabbitTemplate = rabbitTemplate;
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
        Payment savedPayment = paymentRepository.save(paymentToSave);
        AuditDTO auditDTO = new AuditDTO(PAYMENT_ADDED_MESSAGE + savedPayment);
        rabbitTemplate.convertAndSend(AUDIT_QUEUE, auditDTO);
        return savedPayment.getId();
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
        Payment savedPayment = paymentRepository.save(paymentToChangeAvailability);
        String message;
        if (status) {
            message = PAYMENT_ENABLED_MESSAGE + savedPayment;
        } else {
            message = PAYMENT_DISABLED_MESSAGE + savedPayment;
        }
        AuditDTO auditDTO = new AuditDTO(message);
        rabbitTemplate.convertAndSend(AUDIT_QUEUE, auditDTO);
    }
}
