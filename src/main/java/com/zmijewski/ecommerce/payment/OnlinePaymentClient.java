package com.zmijewski.ecommerce.payment;

import com.zmijewski.ecommerce.payment.model.PaymentServiceRequest;
import com.zmijewski.ecommerce.payment.model.PaymentServiceResponse;

public interface OnlinePaymentClient {
    PaymentServiceResponse createPayment(PaymentServiceRequest paymentServiceRequest);
}
