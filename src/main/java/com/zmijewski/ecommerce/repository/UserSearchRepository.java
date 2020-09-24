package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.enums.ProductSortType;
import com.zmijewski.ecommerce.enums.UserSortType;
import com.zmijewski.ecommerce.model.Product;
import com.zmijewski.ecommerce.model.User;
import org.springframework.data.domain.Page;

public interface UserSearchRepository {
    Page<User> searchForUsers(String searchWord, int page, int size, UserSortType userSortType);
}
