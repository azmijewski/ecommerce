package com.zmijewski.ecommerce.model.entity;

import com.zmijewski.ecommerce.model.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "shipping-generator")
    @SequenceGenerator(name = "shipping-generator", sequenceName = "next_shipping_id")
    private Long id;
    private String name;
    private BigDecimal price;
    private Boolean isAvailable;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

}
