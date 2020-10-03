package com.zmijewski.ecommerce.specification;

import com.zmijewski.ecommerce.model.entity.User;
import com.zmijewski.ecommerce.model.enums.UserSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

public class UserSearchSpecification implements Specification<User> {

    public UserSearchSpecification(Map<UserSearchCriteria, String> searchCriteria) {
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
