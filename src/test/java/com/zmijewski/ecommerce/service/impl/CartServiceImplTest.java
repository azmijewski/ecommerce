package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.CartDTO;
import com.zmijewski.ecommerce.exception.CartNotFoundException;
import com.zmijewski.ecommerce.exception.NotEnoughProductException;
import com.zmijewski.ecommerce.exception.ProductNotFoundException;
import com.zmijewski.ecommerce.mapper.CartMapper;
import com.zmijewski.ecommerce.model.Cart;
import com.zmijewski.ecommerce.model.CartProduct;
import com.zmijewski.ecommerce.model.Product;
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

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    @Test
    void shouldAddProductToCartAddProductIfCartNotContain() {
        //given
        Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(new Cart()));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).decreaseProductQuantity(anyLong(), anyInt());
        when(cartProductRepository.findByCartAndProduct(any(), any())).thenReturn(Optional.empty());
        when(cartProductRepository.save(any())).thenReturn(new CartProduct());
        doNothing().when(cartRepository).updateCartPrice(any(), anyLong());
        //when
        cartService.addProductToCart(1L, 1L, 1);
        //then
        verify(cartProductRepository).save(any());
    }
    @Test
    void shouldAddProductToCartModifyProductQuantityInCartIfAlreadyContain() {
        //given
        Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        CartProduct cartProduct = new CartProduct();
        cartProduct.setQuantity(1);
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(new Cart()));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).decreaseProductQuantity(anyLong(), anyInt());
        when(cartProductRepository.findByCartAndProduct(any(), any())).thenReturn(Optional.of(cartProduct));
        when(cartProductRepository.save(any())).thenReturn(new CartProduct());
        doNothing().when(cartRepository).updateCartPrice(any(), anyLong());
        //when
        cartService.addProductToCart(1L, 1L, 1);
        //then
        verify(cartProductRepository).save(any());
    }
    @Test
    void shouldNotAddProductToCartIfCartNotFound() {
        // given
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(CartNotFoundException.class,
                () -> cartService.addProductToCart(1L, 1L, 1));
    }
    @Test
    void shouldNotAddProductToCartIfProductNotFound() {
        //given
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(new Cart()));
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(ProductNotFoundException.class,
                () -> cartService.addProductToCart(1L, 1L, 1));
    }
    @Test
    void shouldNotAddProductToCartIfQuantityIsMoreThenProductQuantity() {
        //given
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(new Cart()));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        doThrow(ConstraintViolationException.class).when(productRepository).decreaseProductQuantity(anyLong(), anyInt());
        //when && then
        assertThrows(NotEnoughProductException.class,
                () -> cartService.addProductToCart(1L, 1L, 1));
    }
    @Test
    void shouldDeleteProductFromCartDeleteIfQuantityIsEqualsToProductQuantityInCart() {
        //given
        Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        CartProduct cartProduct = new CartProduct();
        cartProduct.setQuantity(1);
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(new Cart()));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartProductRepository.findByCartAndProduct(any(), any())).thenReturn(Optional.of(cartProduct));
        doNothing().when(cartProductRepository).delete(any());
        doNothing().when(productRepository).increaseProductQuantity(anyLong(), anyInt());
        doNothing().when(cartRepository).updateCartPrice(any(), anyLong());
        //when
        cartService.deleteProductFromCart(1L, 1L, 1);
        //then
        verify(cartProductRepository).delete(any());
    }
    @Test
    void shouldDeleteProductFromCartModifyProductQuantityInCart() {
        //given
        Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        CartProduct cartProduct = new CartProduct();
        cartProduct.setQuantity(2);
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(new Cart()));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartProductRepository.findByCartAndProduct(any(), any())).thenReturn(Optional.of(cartProduct));
        doNothing().when(productRepository).increaseProductQuantity(anyLong(), anyInt());
        doNothing().when(cartRepository).updateCartPrice(any(), anyLong());
        when(cartProductRepository.save(any())).thenReturn(new CartProduct());
        //when
        cartService.deleteProductFromCart(1L, 1L, 1);
        //then
        verify(cartProductRepository).save(any());
    }
    @Test
    void shouldNotDeleteProductFromCartWhenQuantityIsMoreThenProductQuantityInCart() {
        //given
        CartProduct cartProduct = new CartProduct();
        cartProduct.setQuantity(1);
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(new Cart()));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        when(cartProductRepository.findByCartAndProduct(any(), any())).thenReturn(Optional.of(cartProduct));
        //when && then
        assertThrows(IllegalArgumentException.class,
                () -> cartService.deleteProductFromCart(1L, 1L, 2));
    }

}