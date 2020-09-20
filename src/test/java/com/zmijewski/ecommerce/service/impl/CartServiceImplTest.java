package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.CartDTO;
import com.zmijewski.ecommerce.exception.CartNotFoundException;
import com.zmijewski.ecommerce.mapper.CartMapper;
import com.zmijewski.ecommerce.model.Cart;
import com.zmijewski.ecommerce.repository.CartProductRepository;
import com.zmijewski.ecommerce.repository.CartRepository;
import com.zmijewski.ecommerce.repository.ProductRepository;
import com.zmijewski.ecommerce.repository.UserRepository;
import com.zmijewski.ecommerce.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    @InjectMocks
    CartServiceImpl cartService;
    @Mock
    CartRepository cartRepository;
    @Mock
    CartProductRepository cartProductRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    CartMapper cartMapper;
    @Mock
    UserRepository userRepository;

    @Test
    void shouldGetByIdIfExist() {
        //given
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(new Cart()));
        when(cartMapper.mapToDTO(any())).thenReturn(new CartDTO());
        //when
        CartDTO result = cartService.getById(1L);
        //then
        assertNotNull(result);

    }
    @Test
    void shouldNotGetByIdIfNotExist() {
        //given
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(CartNotFoundException.class, () -> cartService.getById(1L));
    }
    @Test
    void shouldGetByUserIfExist() {
        //given
        when(cartRepository.findByUserMail(anyString())).thenReturn(Optional.of(new Cart()));
        when(cartMapper.mapToDTO(any())).thenReturn(new CartDTO());
        //when
        CartDTO result = cartService.getCartByUser("test@test");
        //then
        assertNotNull(result);
    }
    @Test
    void shouldNotGetByUseIfNotExist() {
        //given
        when(cartRepository.findByUserMail(anyString())).thenReturn(Optional.empty());
        //when && then
        assertThrows(CartNotFoundException.class, () -> cartService.getCartByUser("test@test"));
    }
    @Test
    void shouldCreateNewCart() {
        //given
        when(cartRepository.save(any())).thenReturn(new Cart());
        when(cartMapper.mapToDTO(any())).thenReturn(new CartDTO());
        //when
        CartDTO result = cartService.createNewCart();
        //then
        assertNotNull(result);
    }
}