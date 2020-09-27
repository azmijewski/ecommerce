package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.AddressDTO;
import com.zmijewski.ecommerce.exception.AddressNotFoundException;
import com.zmijewski.ecommerce.exception.UserNotFoundException;
import com.zmijewski.ecommerce.mapper.AddressMapper;
import com.zmijewski.ecommerce.model.entity.Address;
import com.zmijewski.ecommerce.model.entity.User;
import com.zmijewski.ecommerce.repository.AddressRepository;
import com.zmijewski.ecommerce.repository.UserRepository;
import com.zmijewski.ecommerce.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    @InjectMocks
    AddressServiceImpl addressService;
    @Mock
    UserRepository userRepository;
    @Mock
    AddressMapper addressMapper;
    @Mock
    AddressRepository addressRepository;

    @Test
    void shouldGetAddressesForUser() {
        //given
        when(addressRepository.getUserAddresses(anyString())).thenReturn(Collections.singletonList(new Address()));
        when(addressMapper.mapToDTO(any())).thenReturn(new AddressDTO());
        //when
        List<AddressDTO> result = addressService.getAddressesForUser("test@test");
        //then
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldAddAddressForUser() {
        //given
        Long expectedId = 1L;
        Address savedAddress = Address.builder()
                .id(expectedId)
                .build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(addressMapper.mapToAddress(any())).thenReturn(new Address());
        when(addressRepository.save(any())).thenReturn(savedAddress);
        //when
        Long result = addressService.addAddressForUser("teest@test", new AddressDTO());
        //then
        assertEquals(expectedId, result);
    }

    @Test
    void shouldNotAddAddressForUserIfUserNotFound() {
        //given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        //when && then
        assertThrows(UserNotFoundException.class,
                () -> addressService.addAddressForUser("teest@test", new AddressDTO()));
    }
    @Test
    void shouldDeleteAddress() {
        //given
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(new Address()));
        doNothing().when(addressRepository).delete(any());
        //when
        addressService.deleteAddress(1L);
        //them
        verify(addressRepository).delete(any());
    }
    @Test
    void shouldNotDeleteIfAddressNotFound() {
        //given
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(AddressNotFoundException.class,
                () -> addressService.deleteAddress(1L));
    }
    @Test
    void shouldModify() {
        //given
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(new Address()));
        doNothing().when(addressMapper).mapDataTuUpdate(any(), any());
        when(addressRepository.save(any())).thenReturn(new Address());
        //when
        addressService.modifyAddress(1L, new AddressDTO());
        //them
        verify(addressRepository).save(any());
    }
    @Test
    void shouldNotModifyIfAddressNotFound() {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(AddressNotFoundException.class,
                () -> addressService.modifyAddress(1L, new AddressDTO()));
    }


}