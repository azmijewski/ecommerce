package com.zmijewski.ecommerce.model.entity;

import com.zmijewski.ecommerce.listeners.AuditListener;
import com.zmijewski.ecommerce.model.Auditable;
import com.zmijewski.ecommerce.model.enums.AuditObjectType;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@EntityListeners({AuditListener.class})
public class Payment implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment-generator")
    @SequenceGenerator(name = "payment-generator", sequenceName = "next_payment_id")
    private Long id;
    private String name;
    private Boolean isAvailable;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Override
    @Transient
    public AuditObjectType getObjectType() {
        return AuditObjectType.PAYMENT;
    }
}
