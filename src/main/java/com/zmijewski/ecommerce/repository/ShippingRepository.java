package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.entity.Shipping;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {
    @Query("select s from Shipping s where s.isAvailable = true and s.paymentType = :paymentType")
    List<Shipping> findAllActiveByPaymentType(@Param("paymentType") PaymentType paymentType);
}
