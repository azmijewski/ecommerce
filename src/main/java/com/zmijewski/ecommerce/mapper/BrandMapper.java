package com.zmijewski.ecommerce.mapper;

import com.zmijewski.ecommerce.dto.BrandDTO;
import com.zmijewski.ecommerce.model.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandDTO mapToDTO(Brand brand);
    Brand mapToBrand(BrandDTO brandDTO);
    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    void mapDataToUpdate(@MappingTarget Brand brand, BrandDTO brandDTO);
}
