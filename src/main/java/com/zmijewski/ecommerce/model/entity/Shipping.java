package com.zmijewski.ecommerce.model.entity;

import com.zmijewski.ecommerce.listeners.AuditListener;
import com.zmijewski.ecommerce.model.Auditable;
import com.zmijewski.ecommerce.model.enums.AuditObjectType;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.security.DenyAll;
import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@EntityListeners({AuditListener.class})
public class Shipping implements Auditable {
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

    @Override
    public AuditObjectType getObjectType() {
        return AuditObjectType.SHIPPING;
    }
}
