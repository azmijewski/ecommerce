package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProduct.OrderProductPK> {
}
