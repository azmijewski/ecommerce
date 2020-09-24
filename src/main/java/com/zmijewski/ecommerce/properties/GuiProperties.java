package com.zmijewski.ecommerce.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gui")
@Getter
@Setter
public class GuiProperties {
    private String registrationUrl;
}
