package com.zmijewski.ecommerce.dto;

import com.zmijewski.ecommerce.model.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class OrderDTO implements Serializable {
    private Long id;
    private BigDecimal totalPrice;
    private Date createdAt;
    private Date updatedAt;
    private OrderStatus orderStatus;
    private String shippingName;
    private String paymentName;
    private List<ShortProductDTO> products = new ArrayList<>();
}
