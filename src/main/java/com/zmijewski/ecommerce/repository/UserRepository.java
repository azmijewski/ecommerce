package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("select u from User u where u.email = :email and u.isActive = true")
    Optional<User> findActivatedUserByEmail(@Param("email") String email);

    Optional<User> findByToken(String token);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail( String email);

    @Query("select case when count(u) > 0 then true else false end from User u where u.email = :email and u.id <> :id")
    Boolean existsByEmailAndOtherId(@Param("email") String email, @Param("id") Long id);

    @EntityGraph("user with address")
    Optional<User> findWithAddressesByEmail(@Param("email") String email);

}
