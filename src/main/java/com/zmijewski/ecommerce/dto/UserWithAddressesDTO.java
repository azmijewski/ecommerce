package com.zmijewski.ecommerce.dto;

import com.zmijewski.ecommerce.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class UserWithAddressesDTO {
    private UserDTO user;
    private List<AddressDTO> addresses = new ArrayList<>();
}
