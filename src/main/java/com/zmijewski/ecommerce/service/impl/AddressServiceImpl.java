package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.AddressDTO;
import com.zmijewski.ecommerce.exception.AddressNotFoundException;
import com.zmijewski.ecommerce.exception.UserNotFoundException;
import com.zmijewski.ecommerce.mapper.AddressMapper;
import com.zmijewski.ecommerce.model.Address;
import com.zmijewski.ecommerce.model.User;
import com.zmijewski.ecommerce.repository.AddressRepository;
import com.zmijewski.ecommerce.repository.UserRepository;
import com.zmijewski.ecommerce.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressMapper addressMapper;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressMapper addressMapper, UserRepository userRepository, AddressRepository addressRepository) {
        this.addressMapper = addressMapper;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<AddressDTO> getAddressesForUser(String email) {
        return addressRepository.getUserAddresses(email)
                .stream()
                .map(addressMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long addAddressForUser(String email, AddressDTO addressDTO) {
        User userToAddAddress = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
        Address address = addressMapper.mapToAddress(addressDTO);
        address.setUser(userToAddAddress);
        return addressRepository.save(address)
                .getId();
    }

    @Override
    public void deleteAddress(Long addressId) {
        Address addressToDelete = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Could not find address with id: " + addressId));
        addressRepository.delete(addressToDelete);
    }

    @Override
    public void modifyAddress(Long addressId, AddressDTO addressDTO) {
        Address addressToModify = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Could not find address with id: " + addressId));
        addressMapper.mapDataTuUpdate(addressToModify, addressDTO);
        addressRepository.save(addressToModify);
    }
}
