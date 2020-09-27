package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select case when count(p) > 0 then true else false end FROM Product p where p.category.id = :categoryId")
    Boolean hasAnyNoProduct(@Param("categoryId") Long categoryId);
}
