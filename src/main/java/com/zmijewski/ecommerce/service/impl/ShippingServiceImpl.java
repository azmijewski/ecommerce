package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.ShippingDTO;
import com.zmijewski.ecommerce.exception.ShippingNotFoundException;
import com.zmijewski.ecommerce.mapper.ShippingMapper;
import com.zmijewski.ecommerce.model.entity.Shipping;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import com.zmijewski.ecommerce.repository.ShippingRepository;
import com.zmijewski.ecommerce.service.ShippingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShippingServiceImpl implements ShippingService {

    private final ShippingRepository shippingRepository;
    private final ShippingMapper shippingMapper;

    public ShippingServiceImpl(ShippingRepository shippingRepository, ShippingMapper shippingMapper) {
        this.shippingRepository = shippingRepository;
        this.shippingMapper = shippingMapper;
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
        return shippingRepository.save(shippingToSave)
                .getId();
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
        shippingRepository.save(shippingToChangeAvailability);
    }
}
