package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CartRepository extends PagingAndSortingRepository<Cart, Long> {
    @Query("select c from Cart c where c.user.email = :email")
    Optional<Cart> findByUserMail(@Param("email") String mail);

    @Modifying
    @Query("update Cart c set c.totalPrice = c.totalPrice + :price where c.id = :id")
    void updateCartPrice(@Param("price")BigDecimal price, @Param("id") Long cartId);
}
