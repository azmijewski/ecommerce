package com.zmijewski.ecommerce.util;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmailTemplateCreator {

    private static final String LINK = "link";
    private static final String PASSWORD = "password";

    private static final String REGISTRATION_TEMPLATE = "registration-template";
    private static final String USER_ADDED_TEMPLATE = "user-added-template";
    private static final String RESET_PASSWORD_TEMPLATE = "reset-password-template";


    private final TemplateEngine templateEngine;

    public EmailTemplateCreator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String getRegistrationTemplate(String confirmUrl) {
        Context context = new Context();
        context.setVariable(LINK, confirmUrl);
        return templateEngine.process(REGISTRATION_TEMPLATE, context);
    }
    public String getUserAddedTemplate(String password) {
        Context context = new Context();
        context.setVariable(PASSWORD, password);
        return templateEngine.process(USER_ADDED_TEMPLATE, context);
    }
    public String getResetPasswordTemplate(String newPassword) {
        Context context = new Context();
        context.setVariable(PASSWORD, newPassword);
        return templateEngine.process(RESET_PASSWORD_TEMPLATE, context);
    }
}
