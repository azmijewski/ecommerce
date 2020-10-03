package com.zmijewski.ecommerce.util;

import com.zmijewski.ecommerce.model.enums.GlobalParameterName;
import com.zmijewski.ecommerce.model.enums.OrderStatus;
import com.zmijewski.ecommerce.repository.GlobalParameterRepository;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmailTemplateCreator {

    private final GlobalParameterRepository globalParameterRepository;

    private static final String LINK = "link";
    private static final String PASSWORD = "password";
    private static final String ID = "id";
    private static final String STATUS = "status";
    private static final String DAYS = "days";


    private static final String REGISTRATION_TEMPLATE = "registration-template";
    private static final String USER_ADDED_TEMPLATE = "user-added-template";
    private static final String RESET_PASSWORD_TEMPLATE = "reset-password-template";
    private static final String ORDER_CREATED_TEMPLATE = "order-created-template";
    private static final String ORDER_STATUS_CHANGED_TEMPLATE = "order-status-changed-template";
    private static final String ORDER_NEAR_EXPIRED_TEMPLATE = "order-near-expired-template";


    private final TemplateEngine templateEngine;

    public EmailTemplateCreator(GlobalParameterRepository globalParameterRepository, TemplateEngine templateEngine) {
        this.globalParameterRepository = globalParameterRepository;
        this.templateEngine = templateEngine;
    }

    public String getRegistrationTemplate(String token) {
        String confirmUrl = globalParameterRepository.getValueAsString(GlobalParameterName.REGISTRATION_URL) + token;
        Context context = new Context();
        context.setVariable(LINK, confirmUrl);
        return templateEngine.process(REGISTRATION_TEMPLATE, context);
    }
    public String getUserAddedTemplate(String password) {
        Context context = new Context();
        context.setVariable(PASSWORD, password);
        return templateEngine.process(USER_ADDED_TEMPLATE, context);
    }
    public String getResetPasswordTemplate(String token) {
        String confirmUrl = globalParameterRepository.getValueAsString(GlobalParameterName.REGISTRATION_URL) + token;
        Context context = new Context();
        context.setVariable(LINK, confirmUrl);
        return templateEngine.process(RESET_PASSWORD_TEMPLATE, context);
    }
    public String getOrderCreatedTemplate(Long orderId) {
        String checkOrderUrl = globalParameterRepository.getValueAsString(GlobalParameterName.CHECK_ORDER_URL) + orderId;
        Context context = new Context();
        context.setVariable(LINK, checkOrderUrl);
        return templateEngine.process(ORDER_CREATED_TEMPLATE, context);
    }
    public String getOrderStatusChangedTemplate(Long orderId, OrderStatus orderStatus) {
        Context context = new Context();
        GlobalParameterName parameterForStatus = GlobalParameterName.getByOrderStatus(orderStatus);
        String status = globalParameterRepository.getValueAsString(parameterForStatus);
        context.setVariable(ID, orderId);
        context.setVariable(STATUS, status);
        return templateEngine.process(ORDER_STATUS_CHANGED_TEMPLATE, context);
    }
    public String getOrderNearExpireTemplate(Long orderId, Integer days) {
        Context context = new Context();
        context.setVariable(ID, orderId);
        context.setVariable(DAYS, days);
        return templateEngine.process(ORDER_NEAR_EXPIRED_TEMPLATE, context);
    }
}
