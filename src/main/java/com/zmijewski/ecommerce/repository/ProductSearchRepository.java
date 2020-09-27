package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.enums.ProductSortType;
import com.zmijewski.ecommerce.model.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductSearchRepository {

    Page<Product> searchForProduct(String searchWord, int page, int size, ProductSortType productSortType);
}
