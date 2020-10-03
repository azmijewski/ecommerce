package com.zmijewski.ecommerce.repository.impl;

import com.zmijewski.ecommerce.model.entity.User;
import com.zmijewski.ecommerce.model.enums.UserSortType;
import com.zmijewski.ecommerce.repository.UserSearchRepository;
import org.apache.lucene.search.*;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.Objects;

@Repository
public class UserSearchRepositoryImpl implements UserSearchRepository {
    
    private static final String DELIMETER = "\\s";
    private static final String EMAIL_FIELD = "email";
    private static final String FIRST_NAME_FIELD = "firstName";
    private static final String LAST_NAME_FIELD = "lastNamee";
    private static final String PHONE_FIELD = "phoneNumber";
    private static final String ROLE_FIELD = "role.name";
    private static final String ADDRESS_STREET_FIELD = "addresses.street";
    private static final String ADDRESS_CITY_FIELD = "addresses.city";
    private static final String ADDRESS_POSTAL_CODE_FIELD = "addresses.postalCode";
    private static final String ORDERS_CREATED_AT_FIELD = "orders.createdAt";
    private static final String ORDERS_TOKEN_FIELD = "orders.token";
    private static final String DESCENDING_TYPE = "desc";



    private final EntityManager em;

    public UserSearchRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Page<User> searchForUsers(String searchWord, int page, int size, UserSortType userSortType) {
        String[] searchWords = getSearchWords(searchWord);
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        QueryBuilder queryBuilder = fullTextEntityManager
                .getSearchFactory()
                .buildQueryBuilder()
                .forEntity(User.class)
                .get();
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (String word : searchWords) {
            Query query = queryBuilder.keyword()
                    .onFields(EMAIL_FIELD,
                            FIRST_NAME_FIELD,
                            LAST_NAME_FIELD,
                            PHONE_FIELD,
                            ROLE_FIELD,
                            ADDRESS_STREET_FIELD,
                            ADDRESS_CITY_FIELD,
                            ADDRESS_POSTAL_CODE_FIELD,
                            ORDERS_CREATED_AT_FIELD,
                            ORDERS_TOKEN_FIELD)
                    .matching(word)
                    .createQuery();
            builder.add(query, BooleanClause.Occur.MUST);
        }
        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(builder.build(), User.class);
        Sort sort;
        if (userSortType.getSortType().equals(DESCENDING_TYPE)) {
            sort = new Sort(SortField.FIELD_SCORE,
                    new SortField(userSortType.getField(), userSortType.getSortFieldType(), true));
        } else {
            sort = new Sort(SortField.FIELD_SCORE,
                    new SortField(userSortType.getField(), userSortType.getSortFieldType()));
        }
        jpaQuery.setSort(sort);
        long totalElements = jpaQuery.getResultSize();
        jpaQuery.setFirstResult(page * size);
        jpaQuery.setMaxResults(size);

        return new PageImpl<>(jpaQuery.getResultList(), PageRequest.of(page, size), totalElements);
    }
    private String[] getSearchWords(String searchWord) {
        String[] searchWords = StringUtils.split(searchWord, DELIMETER);
        if (Objects.isNull(searchWords)) {
            return new String[]{searchWord};
        }
        return searchWords;
    }
}
