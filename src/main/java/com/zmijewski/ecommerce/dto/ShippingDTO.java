package com.zmijewski.ecommerce.dto;

import com.zmijewski.ecommerce.model.enums.PaymentType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
@NoArgsConstructor
@Data
public class ShippingDTO implements Serializable {
    private Long id;
    @NotBlank
    private String name;
    private BigDecimal price;
    private Boolean isAvailable;
    @NotNull
    private PaymentType paymentType;
}
