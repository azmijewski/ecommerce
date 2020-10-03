package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    @Query("select o from Order o where o.user.email = :email")
    Page<Order> getOrderByEmail(@Param("email") String email, Pageable pageable);

    @Query("select o from Order o where o.token = :token")
    Optional<Order> getOrderByToken(@Param("token") String token);

    Optional<Order> findByPaymentToken(String paymentToken);

    @Query(value="call get_expire_orders_waiting_for_payment(:date)", nativeQuery = true)
    List<Long> getExpiredOrdersIdWhereWaitingForPayment(@Param("date") Date date);

    @Query(value = "call get_orders_waiting_for_payment_to_expire_in(:date, :days)", nativeQuery = true)
    List<Order> getOrdersToExpiredInDays(@Param("date") Date date, @Param("days") Integer days);
}
