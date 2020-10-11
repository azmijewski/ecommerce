package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.AuditDTO;
import com.zmijewski.ecommerce.dto.ShippingDTO;
import com.zmijewski.ecommerce.exception.ShippingNotFoundException;
import com.zmijewski.ecommerce.mapper.ShippingMapper;
import com.zmijewski.ecommerce.model.entity.Shipping;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import com.zmijewski.ecommerce.repository.ShippingRepository;
import com.zmijewski.ecommerce.service.ShippingService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShippingServiceImpl implements ShippingService {

    private static final String SHIPPING_ADDED_MESSAGE = "Added new shipping: ";
    private static final String SHIPPING_ENABLED_MESSAGE = "Enabled shipping: ";
    private static final String SHIPPING_DISABLED_MESSAGE = "Disabled shipping: ";

    private static final String AUDIT_QUEUE = "auditQueue";

    private final ShippingRepository shippingRepository;
    private final ShippingMapper shippingMapper;
    private final RabbitTemplate rabbitTemplate;


    public ShippingServiceImpl(ShippingRepository shippingRepository,
                               ShippingMapper shippingMapper,
                               RabbitTemplate rabbitTemplate) {
        this.shippingRepository = shippingRepository;
        this.shippingMapper = shippingMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public List<ShippingDTO> getAll() {
        return shippingRepository.findAll()
                .stream()
                .map(shippingMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShippingDTO> getAllActiveByType(PaymentType paymentType) {
        return shippingRepository.findAllActiveByPaymentType(paymentType)
                .stream()
                .map(shippingMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ShippingDTO getById(Long shippingId) {
        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new ShippingNotFoundException("Sould not find shipping with id: " + shippingId));
        return shippingMapper.mapToDTO(shipping);
    }

    @Override
    public Long saveShipping(ShippingDTO shippingDTO) {
        Shipping shippingToSave = shippingMapper.mapToShipping(shippingDTO);
        Shipping savedShipping = shippingRepository.save(shippingToSave);
        AuditDTO auditDTO = new AuditDTO(SHIPPING_ADDED_MESSAGE + savedShipping);
        rabbitTemplate.convertAndSend(AUDIT_QUEUE, auditDTO);
        return savedShipping.getId();
    }

    @Override
    public void enableShipping(Long shippingId) {
        changeAvailability(shippingId, true);
    }

    @Override
    public void disableShipping(Long shippingId) {
        changeAvailability(shippingId, false);
    }
    private void changeAvailability(Long shippingId, boolean status) {
        Shipping shippingToChangeAvailability = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new ShippingNotFoundException("Sould not find shipping with id: " + shippingId));
        shippingToChangeAvailability.setIsAvailable(status);
        Shipping savedShipping = shippingRepository.save(shippingToChangeAvailability);
        String message;
        if (status) {
            message = SHIPPING_ENABLED_MESSAGE + savedShipping;
        } else {
            message = SHIPPING_DISABLED_MESSAGE + savedShipping;
        }
        AuditDTO auditDTO = new AuditDTO(message);
        rabbitTemplate.convertAndSend(AUDIT_QUEUE, auditDTO);
    }
}
