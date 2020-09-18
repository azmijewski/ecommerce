package com.zmijewski.ecommerce.model;

import com.zmijewski.ecommerce.enums.PaymentType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@Data
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

}
