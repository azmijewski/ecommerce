package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.entity.User;
import com.zmijewski.ecommerce.model.enums.UserSortType;
import org.springframework.data.domain.Page;

public interface UserSearchRepository {
    Page<User> searchForUsers(String searchWord, int page, int size, UserSortType userSortType);
}
