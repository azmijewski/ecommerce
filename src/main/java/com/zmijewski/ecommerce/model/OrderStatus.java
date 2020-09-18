package com.zmijewski.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order-status-generator")
    @SequenceGenerator(name = "order-status-generator", sequenceName = "next_order_status_id")
    private Long id;
    private String description;
}
