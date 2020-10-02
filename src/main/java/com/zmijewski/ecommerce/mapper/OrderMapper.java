package com.zmijewski.ecommerce.mapper;

import com.zmijewski.ecommerce.dto.CreatedOrderDTO;
import com.zmijewski.ecommerce.dto.OrderDTO;
import com.zmijewski.ecommerce.dto.ShortOrderDTO;
import com.zmijewski.ecommerce.model.entity.Order;
import com.zmijewski.ecommerce.model.entity.OrderProduct;
import com.zmijewski.ecommerce.model.entity.Product;
import com.zmijewski.ecommerce.payment.model.CreateOrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface OrderMapper {
    ShortOrderDTO mapToShortOrderDTO(Order order);
    @Mappings({
            @Mapping(target = "shippingName", source = "shipping.name"),
            @Mapping(target = "paymentName", source = "payment.name"),
            @Mapping(target = "products", source = "orderProducts")
    })
    OrderDTO mapToDTO(Order order);
    @Mappings({
            @Mapping(target = "shippingName", source = "shipping.name"),
            @Mapping(target = "paymentName", source = "payment.name"),
            @Mapping(target = "products", source = "orderProducts")
    })
    CreatedOrderDTO mapToCreatedOrderDTO(Order order);
    default Product getProduct(OrderProduct orderProduct) {
        if (Objects.nonNull(orderProduct)) {
            return orderProduct.getProduct();
        }
        return null;
    }
}
