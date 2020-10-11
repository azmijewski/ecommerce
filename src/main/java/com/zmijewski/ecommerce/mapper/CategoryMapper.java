package com.zmijewski.ecommerce.mapper;

import com.zmijewski.ecommerce.dto.CategoryDTO;
import com.zmijewski.ecommerce.model.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
//    @Mappings({
//            @Mapping(target = "imageId", source = "image.id")
//    })
    CategoryDTO mapToDTO(Category category);
    Category mapToCategory(CategoryDTO categoryDTO);
    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    void mapDataToUpdate(@MappingTarget Category category, CategoryDTO categoryDTO);
}
