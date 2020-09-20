package com.zmijewski.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@Data
public class CartDTO {
    private Long id;
    private BigDecimal totalPrice;
    private List<CartProductDTO> products = new ArrayList<>();
}
