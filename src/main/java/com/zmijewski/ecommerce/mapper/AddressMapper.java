package com.zmijewski.ecommerce.mapper;

import com.zmijewski.ecommerce.dto.AddressDTO;
import com.zmijewski.ecommerce.model.Address;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address mapToAddress(AddressDTO addressDTO);
    AddressDTO mapToDTO(Address address);
}
