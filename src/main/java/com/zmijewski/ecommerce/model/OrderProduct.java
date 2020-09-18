package com.zmijewski.ecommerce.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Data
@Entity
public class OrderProduct {

    @EmbeddedId
    private OrderProductPK id = new OrderProductPK();


    @MapsId("orderId")
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @MapsId("productId")
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;


    @NoArgsConstructor
    @Data
    @Embeddable
    public static class OrderProductPK implements Serializable {
        private Long orderId;
        private Long productId;
    }
}
