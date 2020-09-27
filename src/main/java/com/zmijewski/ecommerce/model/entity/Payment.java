package com.zmijewski.ecommerce.model.entity;

import com.zmijewski.ecommerce.model.enums.PaymentType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment-generator")
    @SequenceGenerator(name = "payment-generator", sequenceName = "next_payment_id")
    private Long id;
    private String name;
    private Boolean isAvailable;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
}
