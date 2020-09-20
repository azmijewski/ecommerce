package com.zmijewski.ecommerce.mapper;

import com.zmijewski.ecommerce.dto.ProductDTO;
import com.zmijewski.ecommerce.dto.ShortProductDTO;
import com.zmijewski.ecommerce.model.Image;
import com.zmijewski.ecommerce.model.Product;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mappings({
            @Mapping(target = "brand", source = "brand.name"),
            @Mapping(target = "category", source = "category.name")
    })
    ProductDTO mapToDTO(Product product);

    @Mapping(target = "brand", source = "brand.name")
    ShortProductDTO mapToShortProductDTO(Product product);
    @Mappings({
            @Mapping(target = "brand", ignore = true),
            @Mapping(target = "category", ignore = true)
    })
    Product mapToProduct(ProductDTO productDTO);


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "brand", ignore = true),
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "quantity", ignore = true)

    })
    void mapProductToUpdate(@MappingTarget Product productToUpdate, ProductDTO productDTO);

    @AfterMapping
    default ShortProductDTO mapImageIdToShortProductDTO (@MappingTarget ShortProductDTO shortProductDTO, Product product) {
        List<Image> images = product.getImages();
        if(!images.isEmpty()) {
            Long imageId = images.get(0).getId();
            shortProductDTO.setImageId(imageId);
        }
        return shortProductDTO;
    }
    @AfterMapping
    default ProductDTO mapListOfImageIdToProductDTO(@MappingTarget ProductDTO productDTO, Product product) {
        List<Long> ids = product.getImages().stream()
                .map(Image::getId)
                .collect(Collectors.toList());
        productDTO.setImagesIds(ids);
        return productDTO;
    }


}
