package com.zmijewski.ecommerce.mapper;

import com.zmijewski.ecommerce.dto.AddressDTO;
import com.zmijewski.ecommerce.model.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address mapToAddress(AddressDTO addressDTO);
    AddressDTO mapToDTO(Address address);

    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    void mapDataTuUpdate(@MappingTarget Address address, AddressDTO addressDTO);
}
