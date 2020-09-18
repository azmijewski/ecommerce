package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.Payment;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
