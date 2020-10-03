package com.zmijewski.ecommerce.payment.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CompleteOrderResponse {
    private String orderId;
}
