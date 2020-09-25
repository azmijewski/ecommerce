package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAddressesForUser(String email);
    Long addAddressForUser(String email, AddressDTO addressDTO);
    void deleteAddress(Long addressId);
    void modifyAddress(Long addressId, AddressDTO addressDTO);
}
