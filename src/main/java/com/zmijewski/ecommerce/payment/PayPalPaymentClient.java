package com.zmijewski.ecommerce.payment;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.zmijewski.ecommerce.exception.PaymentExecutionException;
import com.zmijewski.ecommerce.model.enums.GlobalParameterName;
import com.zmijewski.ecommerce.payment.model.CompleteOrderRequest;
import com.zmijewski.ecommerce.payment.model.CompleteOrderResponse;
import com.zmijewski.ecommerce.payment.model.CreateOrderRequest;
import com.zmijewski.ecommerce.payment.model.CreateOrderResponse;
import com.zmijewski.ecommerce.properties.PayPalProperties;
import com.zmijewski.ecommerce.repository.GlobalParameterRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Component
@Log4j2
public class PayPalPaymentClient implements OnlinePaymentClient {

    private static final String PAYMENT_METHOD = "paypal";
    private static final String PAYMENT_INTENT = "sale";
    private static final String APPROVAL_URL = "approval_url";

    private final PayPalProperties payPalProperties;
    private APIContext apiContext;
    private final GlobalParameterRepository globalParameterRepository;


    public PayPalPaymentClient(PayPalProperties payPalProperties, GlobalParameterRepository globalParameterRepository) {
        this.payPalProperties = payPalProperties;
        this.globalParameterRepository = globalParameterRepository;
    }
    @PostConstruct
    protected void createApiContext() {
        apiContext = new APIContext(payPalProperties.getClientId(),
                payPalProperties.getClientSecret(), payPalProperties.getEnvironment());
    }

    @Override
    public CreateOrderResponse createPayment(CreateOrderRequest createOrderRequest) {
        Amount amount = new Amount();
        amount.setCurrency(createOrderRequest.getCurrency());
        amount.setTotal(createOrderRequest.getTotalAmount().toString());

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = Collections.singletonList(transaction);

        Payer payer = createPayer(createOrderRequest);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(globalParameterRepository.getValueAsString(GlobalParameterName.REDIRECT_ONLINE_PAYMENT_URL));
        redirectUrls.setCancelUrl(globalParameterRepository.getValueAsString(GlobalParameterName.REDIRECT_CANCEL_ONLINE_PAYMENT_URL));

        Payment payment = new Payment();
        payment.setIntent(PAYMENT_INTENT);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment;
        try {
            createdPayment = payment.create(apiContext);
        } catch (PayPalRESTException e) {
            log.error(e);
            throw new PaymentExecutionException("Could not create payment");
        }
        String redirectUrl = createdPayment.getLinks().stream()
                .filter(link -> APPROVAL_URL.equals(link.getRel()))
                .findFirst()
                .map(Links::getHref)
                .orElseThrow(() -> new PaymentExecutionException("Could not get redirect url from PayPal"));
        CreateOrderResponse response = new CreateOrderResponse();
        response.setRedirectUrl(redirectUrl);
        response.setOrderId(createdPayment.getId());
        return response;
    }

    @Override
    public CompleteOrderResponse completeOrder(CompleteOrderRequest completeOrderRequest) {

        Payment payment = new Payment();
        payment.setId(completeOrderRequest.getPaymentId());

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(completeOrderRequest.getPayerId());

        Payment executedPayment;
        try {
            executedPayment = payment.execute(apiContext, paymentExecution);
        } catch (PayPalRESTException e) {
            log.error(e);
            throw new PaymentExecutionException("Could not execute payment with id: " + completeOrderRequest.getPaymentId());
        }
        return new CompleteOrderResponse(executedPayment.getId());
    }

    private Payer createPayer(CreateOrderRequest request) {
        Payer payer = new Payer();
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setEmail(request.getEmail());
        payerInfo.setFirstName(request.getFirstName());
        payerInfo.setLastName(request.getLastName());
        payer.setPayerInfo(payerInfo);
        payer.setPaymentMethod(PAYMENT_METHOD);
        return payer;
    }

}
