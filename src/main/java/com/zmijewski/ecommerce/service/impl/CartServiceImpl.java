package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.CartDTO;
import com.zmijewski.ecommerce.exception.CartNotFoundException;
import com.zmijewski.ecommerce.exception.NotEnoughProductException;
import com.zmijewski.ecommerce.exception.ProductNotFoundException;
import com.zmijewski.ecommerce.exception.ProductNotInCartException;
import com.zmijewski.ecommerce.mapper.CartMapper;
import com.zmijewski.ecommerce.model.Cart;
import com.zmijewski.ecommerce.model.CartProduct;
import com.zmijewski.ecommerce.model.Product;
import com.zmijewski.ecommerce.model.User;
import com.zmijewski.ecommerce.repository.CartProductRepository;
import com.zmijewski.ecommerce.repository.CartRepository;
import com.zmijewski.ecommerce.repository.ProductRepository;
import com.zmijewski.ecommerce.repository.UserRepository;
import com.zmijewski.ecommerce.service.CartService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartProductRepository cartProductRepository,
                           ProductRepository productRepository,
                           CartMapper cartMapper, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartProductRepository = cartProductRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
        this.userRepository = userRepository;
    }

    @Override
    public CartDTO getById(Long cartId) {
        return cartRepository.findById(cartId)
                .map(cartMapper::mapToDTO)
                .orElseThrow(() -> new CartNotFoundException("Could not find cart with id: " + cartId));
    }

    @Override
    public CartDTO getCartByUser(String email) {
        return cartRepository.findByUserMail(email)
                .map(cartMapper::mapToDTO)
                .orElseThrow(() -> new CartNotFoundException("Could not find cart for user with mail: " + email));
    }

    @Override
    public CartDTO createNewCart() {
        Cart cart = new Cart();
        cart.setTotalPrice(BigDecimal.ZERO);
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.mapToDTO(savedCart);
    }

    @Override
    @Transactional
    public void addProductToCart(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Could not find cart with id: " + cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Could not find product with id: " + productId));
        try {
            productRepository.decreaseProductQuantity(productId, quantity);
        } catch (ConstraintViolationException e) {
            log.error(e);
            throw new NotEnoughProductException("Product with id: " + productId + " has not enough quantity to decrease");
        }
        Optional<CartProduct> optionalCartProduct = cartProductRepository.findByCartAndProduct(cart, product);
        CartProduct cartProduct;
        if (optionalCartProduct.isPresent()) {
            cartProduct = optionalCartProduct.get();
            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);

        } else {
            cartProduct = new CartProduct();
            cartProduct.setCart(cart);
            cartProduct.setProduct(product);
            cartProduct.setQuantity(quantity);
        }
        cartProductRepository.save(cartProduct);
        BigDecimal priceToAdd = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        cartRepository.updateCartPrice(priceToAdd, cartId);
    }

    @Override
    @Transactional
    public void deleteProductFromCart(Long cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Could not find cart with id: " + cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Could not find product with id: " + productId));
        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new ProductNotInCartException("Product with id: " + product + " is not in cart with id: " + cartId));
        if (quantity.equals(cartProduct.getQuantity())) {
            cartProductRepository.delete(cartProduct);
        } else if (quantity < cartProduct.getQuantity()){
            cartProduct.setQuantity(cartProduct.getQuantity() - quantity);
            cartProductRepository.save(cartProduct);
        } else {
            throw new IllegalArgumentException("Product Quantity cannot be lower then 0");
        }
        productRepository.increaseProductQuantity(productId, quantity);
        BigDecimal priceToSubtract = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        cartRepository.updateCartPrice(priceToSubtract.negate(), cartId);
    }

    @Override
    @Transactional
    public void clearCart(Long cartId) {
        cartProductRepository.findAllByCart(cartId).forEach(cartProduct -> {
            Product product = cartProduct.getProduct();
            Integer quantity = cartProduct.getQuantity();
            productRepository.increaseProductQuantity(product.getId(), quantity);
            BigDecimal priceToSubtract = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            cartRepository.updateCartPrice(priceToSubtract.negate(), cartId);
            cartProductRepository.delete(cartProduct);
        });
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {
        Cart cartToDelete = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Could not find cart with id: " + cartId));
        clearCart(cartId);
        cartRepository.delete(cartToDelete);
    }

    @Override
    @Transactional
    public void assignCartToUser(String email, Long cartId) {
        Cart cartToAssignToUser = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Could not find cart with id: " + cartId));
        User userToBeAssigned = userRepository.findActivatedUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user with email: " + email));
        Cart userOldCart = userToBeAssigned.getCart();
        if (Objects.nonNull(userOldCart)) {
            deleteCart(userOldCart.getId());
        }
        cartToAssignToUser.setUser(userToBeAssigned);
        cartRepository.save(cartToAssignToUser);
    }
}
