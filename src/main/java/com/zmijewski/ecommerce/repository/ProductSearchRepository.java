package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.enums.ProductSortType;
import com.zmijewski.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductSearchRepository {

    Page<Product> searchForProduct(String searchWord, int page, int size, ProductSortType productSortType);
}
