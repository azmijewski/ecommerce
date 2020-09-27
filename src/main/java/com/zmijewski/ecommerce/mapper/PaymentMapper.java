package com.zmijewski.ecommerce.mapper;

import com.zmijewski.ecommerce.dto.PaymentDTO;
import com.zmijewski.ecommerce.model.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDTO mapToDTO(Payment payment);
    Payment mapToPayment(PaymentDTO paymentDTO);
}
