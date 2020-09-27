package com.zmijewski.ecommerce.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image-generator")
    @SequenceGenerator(name = "image-generator", sequenceName = "next_image_id")
    private Long id;
    private String name;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] photo;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
