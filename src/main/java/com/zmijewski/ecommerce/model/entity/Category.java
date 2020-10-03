package com.zmijewski.ecommerce.model.entity;

import com.zmijewski.ecommerce.listeners.AuditListener;
import com.zmijewski.ecommerce.model.Auditable;
import com.zmijewski.ecommerce.model.enums.AuditObjectType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Indexed@EntityListeners({AuditListener.class})
@ToString(exclude = {"products"})
public class Category implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category-generator")
    @SequenceGenerator(name = "category-generator", sequenceName = "next_category_id")
    private Long id;
    @Field
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    @Override
    @Transient
    public AuditObjectType getObjectType() {
        return AuditObjectType.CATEGORY;
    }
}
