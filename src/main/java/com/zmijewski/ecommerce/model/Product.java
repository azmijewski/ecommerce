package com.zmijewski.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Indexed
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "product-generator")
    @SequenceGenerator(name = "product-generator", sequenceName = "next_product_id")
    private Long id;
    @Field
    private String name;
    @Min(0)
    private Integer quantity = 0;
    private BigDecimal price;
    private Boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @IndexedEmbedded
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @IndexedEmbedded
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Image> images = new ArrayList<>();
}
