package com.zmijewski.ecommerce.dto;

import com.zmijewski.ecommerce.model.enums.PaymentType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@Data
public class PaymentDTO implements Serializable {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private PaymentType paymentType;
    private Boolean isAvailable;
}
