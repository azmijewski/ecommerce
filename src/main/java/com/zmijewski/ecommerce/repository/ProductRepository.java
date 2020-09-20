package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("select p from Product p where p.isAvailable = true")
    Page<Product> findAllAvailable(Pageable pageable);

    @Modifying
    @Query("update Product p set p.quantity = p.quantity + :quantity where p.id = :id")
    void increaseProductQuantity(@Param("id") Long productId, @Param("quantity") Integer quantityToIncrease);

    @Modifying
    @Query("update Product p set p.quantity = p.quantity - :quantity where p.id = :id")
    void decreaseProductQuantity(@Param("id") Long productId, @Param("quantity") Integer quantityToDecrease);
}
