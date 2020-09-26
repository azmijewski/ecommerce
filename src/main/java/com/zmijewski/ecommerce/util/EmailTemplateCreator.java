package com.zmijewski.ecommerce.util;

import com.zmijewski.ecommerce.properties.GuiProperties;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmailTemplateCreator {

    private final GuiProperties guiProperties;

    private static final String LINK = "link";
    private static final String PASSWORD = "password";

    private static final String REGISTRATION_TEMPLATE = "registration-template";
    private static final String USER_ADDED_TEMPLATE = "user-added-template";
    private static final String RESET_PASSWORD_TEMPLATE = "reset-password-template";


    private final TemplateEngine templateEngine;

    public EmailTemplateCreator(GuiProperties guiProperties, TemplateEngine templateEngine) {
        this.guiProperties = guiProperties;
        this.templateEngine = templateEngine;
    }

    public String getRegistrationTemplate(String token) {
        String confirmUrl = guiProperties.getRegistrationUrl() + token;
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
        String confirmUrl = guiProperties.getRegistrationUrl() + token;
        Context context = new Context();
        context.setVariable(LINK, confirmUrl);
        return templateEngine.process(RESET_PASSWORD_TEMPLATE, context);
    }
}
