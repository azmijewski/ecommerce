package com.zmijewski.ecommerce.payment;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.zmijewski.ecommerce.exception.PaymentCreationException;
import com.zmijewski.ecommerce.payment.model.PaymentServiceRequest;
import com.zmijewski.ecommerce.payment.model.PaymentServiceResponse;
import com.zmijewski.ecommerce.properties.GuiProperties;
import com.zmijewski.ecommerce.properties.PayPalProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class PayPalPaymentClient implements OnlinePaymentClient {

    private static final String PAYMENT_METHOD = "paypal";
    private static final String PAYMENT_INTENT = "sale";
    private static final String APPROVAL_URL = "approval_url";

    private final PayPalProperties payPalProperties;
    private final GuiProperties guiProperties;

    public PayPalPaymentClient(PayPalProperties payPalProperties, GuiProperties guiProperties) {
        this.payPalProperties = payPalProperties;
        this.guiProperties = guiProperties;
    }

    @Override
    public PaymentServiceResponse createPayment(PaymentServiceRequest paymentServiceRequest) {
        Amount amount = new Amount();
        amount.setCurrency(paymentServiceRequest.getCurrency());
        amount.setTotal(paymentServiceRequest.getTotalAmount().toString());

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(PAYMENT_METHOD);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(guiProperties.getRedirectOnlinePaymentUrl());
        redirectUrls.setCancelUrl(guiProperties.getRedirectCancelOnlinePaymentUrl());

        Payment payment = new Payment();
        payment.setIntent(PAYMENT_INTENT);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);
        APIContext apiContext = new APIContext(payPalProperties.getClientId(),
                payPalProperties.getClientSecret(), payPalProperties.getEnvironment());
        Payment createdPayment;
        try {
            createdPayment = payment.create(apiContext);
        } catch (PayPalRESTException e) {
            log.error(e);
            throw new PaymentCreationException("Could not create payment");
        }
        String redirectUrl = createdPayment.getLinks().stream()
                .filter(link -> APPROVAL_URL.equals(link.getRel()))
                .findFirst()
                .map(Links::getHref)
                .orElseThrow(() -> new PaymentCreationException("Could not get redirect url from PayPal"));
        PaymentServiceResponse response = new PaymentServiceResponse();
        response.setRedirectUrl(redirectUrl);
        response.setOrderId(createdPayment.getId());
        return response;
    }

}
