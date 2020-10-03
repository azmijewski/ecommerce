package com.zmijewski.ecommerce.mapper;

import com.zmijewski.ecommerce.dto.CartDTO;
import com.zmijewski.ecommerce.dto.CartProductDTO;
import com.zmijewski.ecommerce.model.entity.Cart;
import com.zmijewski.ecommerce.model.entity.CartProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mappings({
            @Mapping(target = "brand", source = "product.brand.name"),
            @Mapping(target = "name", source = "product.name"),
            @Mapping(target = "price", source = "product.price")
    })
    CartProductDTO mapToCartProductDTO(CartProduct cartProduct);

    @Mapping(target = "products", source = "cartProducts")
    CartDTO mapToDTO(Cart cart);

}
