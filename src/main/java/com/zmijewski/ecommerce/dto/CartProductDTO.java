package com.zmijewski.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class CartProductDTO implements Serializable {
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer quantity;
}
