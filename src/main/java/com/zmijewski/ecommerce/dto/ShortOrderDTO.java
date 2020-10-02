package com.zmijewski.ecommerce.dto;

import com.zmijewski.ecommerce.model.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@Data
public class ShortOrderDTO implements Serializable {
    private Long id;
    private BigDecimal totalPrice;
    private Date createdAt;
    private OrderStatus orderStatus;
}
