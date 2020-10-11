package com.zmijewski.ecommerce.model.entity;

import lombok.*;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Indexed
@ToString(exclude = {"brand", "category", "cartProducts", "orderProducts", "images"})
@NamedEntityGraph(name = "product-graph",
attributeNodes = {
        @NamedAttributeNode(value = "images"),
        @NamedAttributeNode(value = "attributes"),
        @NamedAttributeNode(value = "brand")

})
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
    @Field(analyze = Analyze.NO)
    private BigDecimal price;
    @Field(analyze = Analyze.NO)
    private Boolean isAvailable;
    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @IndexedEmbedded
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @IndexedEmbedded
    private Category category;

    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "value")
    @CollectionTable(name = "product_attribute")
    private Map<String, String> attributes = new HashMap<>();

    @OneToMany(mappedBy = "product")
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Image> images = new ArrayList<>();
}
