package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.CartDTO;

public interface CartService {

    CartDTO getById(Long cartId);
    CartDTO getCartByUser(String email);
    CartDTO createNewCart();
    void addProductToCart(Long cartId, Long productId, Integer quantity);
    void deleteProductFromCart(Long cartId, Long productId, Integer quantity);
    void clearCart(Long cartId);
    void deleteCart(Long cartId);
    void assignCartToUser(String email, Long cartId);

}
