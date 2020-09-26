package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.dto.CartDTO;
import com.zmijewski.ecommerce.service.CartService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/")
@Log4j2
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @GetMapping("carts/{cartId}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable(name = "cartId") Long cartId) {
        log.info("Getting cart with id: {}", cartId);
        CartDTO result = cartService.getById(cartId);
        return ResponseEntity.ok(result);
    }
    @GetMapping("cartByUser")
    @Secured({"ROLE_USER"})
    public ResponseEntity<CartDTO> getCartByUser(Principal principal) {
        log.info("Getting cart for user: {}", principal.getName());
        CartDTO result = cartService.getCartByUser(principal.getName());
        return ResponseEntity.ok(result);
    }
    @GetMapping("newCart")
    public ResponseEntity<CartDTO> getNewCart() {
        log.info("Creating new cart");
        CartDTO result = cartService.createNewCart();
        return ResponseEntity.ok(result);
    }
    @PutMapping("productToCart")
    public ResponseEntity<?> addProductToCart(@RequestParam(name = "productId") Long productId,
                                              @RequestParam(name = "cartId") Long cartId,
                                              @RequestParam(name = "quantity") @Min(1) Integer quantity) {
        log.info("Adding {} products with id: {} to cart with id: {}", quantity, productId, cartId);
        cartService.addProductToCart(cartId, productId, quantity);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("productFromCart")
    public ResponseEntity<?> deleteProductFromCart(@RequestParam(name = "productId") Long productId,
                                              @RequestParam(name = "cartId") Long cartId,
                                              @RequestParam(name = "quantity") @Min(1) Integer quantity) {
        log.info("Deleting {} products with id: {} to cart with id: {}", quantity, productId, cartId);
        cartService.deleteProductFromCart(cartId, productId, quantity);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("carts/{cartId}/products")
    public ResponseEntity<?> clearCart(@PathVariable(name = "cartId") Long cartId) {
        log.info("Clearing cart with id: {}", cartId);
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("carts/{cartId}")
    public ResponseEntity<?> deleteCart(@PathVariable(name = "cartId") Long cartId) {
        log.info("Deleting cart with id: {}", cartId);
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/carts/{cartId}/toUser")
    @Secured({"ROLE_USER"})
    public ResponseEntity<?> assignCartToUser(Principal principal,
                                              @PathVariable(name = "cartId") Long cartId) {
        log.info("Assigning cart with id: {} to user: {}", cartId, principal.getName());
        cartService.assignCartToUser(principal.getName(), cartId);
        return ResponseEntity.noContent().build();
    }

}
