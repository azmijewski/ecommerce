package com.zmijewski.ecommerce.payment;

import com.zmijewski.ecommerce.payment.model.CompleteOrderRequest;
import com.zmijewski.ecommerce.payment.model.CompleteOrderResponse;
import com.zmijewski.ecommerce.payment.model.CreateOrderRequest;
import com.zmijewski.ecommerce.payment.model.CreateOrderResponse;

public interface OnlinePaymentClient {
    CreateOrderResponse createPayment(CreateOrderRequest createOrderRequest);
    CompleteOrderResponse completeOrder(CompleteOrderRequest completeOrderRequest);
}
