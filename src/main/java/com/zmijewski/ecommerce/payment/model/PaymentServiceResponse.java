package com.zmijewski.ecommerce.payment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PaymentServiceResponse {
    private String redirectUrl;
    private String orderId;

}
