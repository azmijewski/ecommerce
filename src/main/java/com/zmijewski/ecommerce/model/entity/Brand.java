package com.zmijewski.ecommerce.model.entity;

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
@ToString(exclude = {"products"})
public class Brand{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand-generator")
    @SequenceGenerator(name = "brand-generator", sequenceName = "next_brand_id")
    private Long id;
    @Field
    private String name;

    @OneToMany(mappedBy = "brand")
    private List<Product> products = new ArrayList<>();

}
