package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.ShippingDTO;
import com.zmijewski.ecommerce.model.enums.PaymentType;

import java.util.List;

public interface ShippingService {
    List<ShippingDTO> getAll();
    List<ShippingDTO> getAllActiveByType(PaymentType paymentType);
    ShippingDTO getById(Long shippingId);
    Long saveShipping(ShippingDTO shippingDTO);
    void enableShipping(Long shippingId);
    void disableShipping(Long shippingId);
}
