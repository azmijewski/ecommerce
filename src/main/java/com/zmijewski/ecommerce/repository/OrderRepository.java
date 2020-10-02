package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    @Query("select o from Order o where o.user.email = :email")
    Page<Order> getOrderByEmail(@Param("email") String email, Pageable pageable);

    @Query("select o from Order o where o.token = :token")
    Optional<Order> getOrderByToken(@Param("token") String token);

    Optional<Order> findByPaymentToken(String paymentToken);
}
