package com.zmijewski.ecommerce.mapper;

import com.zmijewski.ecommerce.dto.ShippingDTO;
import com.zmijewski.ecommerce.model.entity.Shipping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShippingMapper {
    ShippingDTO mapToDTO(Shipping shipping);
    Shipping mapToShipping(ShippingDTO shippingDTO);
}
