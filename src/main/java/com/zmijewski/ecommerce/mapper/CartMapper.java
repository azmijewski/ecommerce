package com.zmijewski.ecommerce.mapper;

import com.zmijewski.ecommerce.dto.CartDTO;
import com.zmijewski.ecommerce.dto.CartProductDTO;
import com.zmijewski.ecommerce.dto.ShortProductDTO;
import com.zmijewski.ecommerce.model.Cart;
import com.zmijewski.ecommerce.model.CartProduct;
import org.mapstruct.*;

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
