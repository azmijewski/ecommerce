package com.zmijewski.ecommerce.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class CartProduct {

    @EmbeddedId
    private CartProductPK id = new CartProductPK();


    @MapsId("cartId")
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @MapsId("productId")
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;


    @NoArgsConstructor
    @Data
    @Embeddable
    public static class CartProductPK implements Serializable {
        private Long cartId;
        private Long productId;
    }
}
