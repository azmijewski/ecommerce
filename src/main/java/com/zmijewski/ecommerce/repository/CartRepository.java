package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.entity.Cart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends PagingAndSortingRepository<Cart, Long> {
    @Query("select distinct c from Cart c left join fetch c.cartProducts cp left join fetch cp.product p where c.user.email = :email")
    Optional<Cart> findByUserMail(@Param("email") String mail);

    @Query("select distinct c from Cart c left join fetch c.cartProducts cp left join fetch cp.product p where c.id = :id")
    Optional<Cart> findWithProductsById(@Param("id") Long id);

    @Modifying
    @Query("update Cart c set c.totalPrice = c.totalPrice + :price where c.id = :id")
    void updateCartPrice(@Param("price")BigDecimal price, @Param("id") Long cartId);

    @Query(value = "call get_old_carts_id(:date)", nativeQuery = true)
    List<Long> getOldCarts(@Param("date") OffsetDateTime date);
}
