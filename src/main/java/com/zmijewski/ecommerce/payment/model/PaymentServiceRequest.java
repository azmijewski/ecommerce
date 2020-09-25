package com.zmijewski.ecommerce.payment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentServiceRequest {
    private BigDecimal totalAmount;
    private String currency = "PLN";
}
