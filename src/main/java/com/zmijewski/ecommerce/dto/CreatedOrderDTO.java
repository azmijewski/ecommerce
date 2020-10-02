package com.zmijewski.ecommerce.dto;

import com.zmijewski.ecommerce.model.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class CreatedOrderDTO {
    private Long id;
    private BigDecimal totalPrice;
    private Date createdAt;
    private OrderStatus orderStatus;
    private String shippingName;
    private String paymentName;
    private List<ShortProductDTO> products = new ArrayList<>();
    private String paymentLink;
    private Boolean payNow;
}
