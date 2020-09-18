package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @Query("select u from User u where u.email = :email and u.isActive = true")
    Optional<User> findActivatedUserByEmail(@Param("email") String email);
}
