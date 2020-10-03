package com.zmijewski.ecommerce.model.entity;

import com.zmijewski.ecommerce.listeners.AuditListener;
import com.zmijewski.ecommerce.model.Auditable;
import com.zmijewski.ecommerce.model.enums.AuditObjectType;
import lombok.*;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Indexed
@EntityListeners({AuditListener.class})
@ToString(exclude = {"products"})
public class Brand implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand-generator")
    @SequenceGenerator(name = "brand-generator", sequenceName = "next_brand_id")
    private Long id;
    @Field
    private String name;

    @OneToMany(mappedBy = "brand")
    private List<Product> products = new ArrayList<>();

    @Override
    @Transient
    public AuditObjectType getObjectType() {
        return AuditObjectType.BRAND;
    }
}
