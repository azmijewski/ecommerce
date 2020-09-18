package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, CartProduct.CartProductPK> {
}
