package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.entity.Payment;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select p from Payment p where p.isAvailable = true  and p.paymentType = :paymentType")
    List<Payment> findAllActiveByType(@Param("paymentType")PaymentType paymentType);
}
