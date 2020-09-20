package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.Cart;
import com.zmijewski.ecommerce.model.CartProduct;
import com.zmijewski.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, CartProduct.CartProductPK> {

    Optional<CartProduct> findByCartAndProduct(Cart cart, Product product);

    @Query("SELECT cp FROM CartProduct cp where cp.cart.id = :id")
    List<CartProduct> findAllByCart(@Param("id") Long cartId);
}
