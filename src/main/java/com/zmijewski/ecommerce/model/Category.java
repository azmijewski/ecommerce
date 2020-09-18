package com.zmijewski.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category-generator")
    @SequenceGenerator(name = "category-generator", sequenceName = "next_category_id")
    private Long id;
    @Field
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

}
