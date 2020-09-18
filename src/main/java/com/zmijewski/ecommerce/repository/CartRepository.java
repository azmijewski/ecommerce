package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.Cart;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends PagingAndSortingRepository<Cart, Long> {
}
