package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @EntityGraph("product-graph")
    @Query("select  p from Product p  where p.isAvailable = true")
    Page<Product> findAllAvailable(Pageable pageable);

    @EntityGraph("product-graph")
    Optional<Product> findById(Long id);

    @Modifying
    @Query("update Product p set p.quantity = p.quantity + :quantity where p.id = :id")
    void increaseProductQuantity(@Param("id") Long productId, @Param("quantity") Integer quantityToIncrease);

    @Modifying
    @Query("update Product p set p.quantity = p.quantity - :quantity where p.id = :id")
    void decreaseProductQuantity(@Param("id") Long productId, @Param("quantity") Integer quantityToDecrease);
}
