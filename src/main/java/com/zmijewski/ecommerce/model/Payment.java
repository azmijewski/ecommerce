package com.zmijewski.ecommerce.model;

import com.zmijewski.ecommerce.enums.PaymentType;
import lombok.AllArgsConstructor;
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
