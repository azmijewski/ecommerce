package com.zmijewski.ecommerce.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "pay-pal")
public class PayPalProperties {
    private String clientId;
    private String clientSecret;
    private String environment;
}
