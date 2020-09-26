package com.zmijewski.ecommerce.payment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateOrderResponse {
    private String redirectUrl;
    private String orderId;

}
