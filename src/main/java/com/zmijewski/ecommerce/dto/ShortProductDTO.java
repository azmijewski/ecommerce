package com.zmijewski.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShortProductDTO implements Serializable {
    private Long id;
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private Long imageId;
}
