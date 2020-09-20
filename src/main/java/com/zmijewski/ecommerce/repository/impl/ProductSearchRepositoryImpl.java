package com.zmijewski.ecommerce.repository.impl;

import com.zmijewski.ecommerce.enums.ProductSortType;
import com.zmijewski.ecommerce.model.Product;
import com.zmijewski.ecommerce.repository.ProductSearchRepository;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.*;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Objects;

@Repository
@Transactional(readOnly = true)
public class ProductSearchRepositoryImpl implements ProductSearchRepository {

    private static final String NAME_FIELD = "name";
    private static final String IS_AVAILABLE_FIELD = "isAvailable";
    private static final String BRAND_FIELD = "brand.name";
    private static final String CATEGORY_FIELD = "category.name";
    private static final String DESCENDING_TYPE = "desc";


    private static final String DELIMETER = "\\s";

    private final EntityManager em;

    public ProductSearchRepositoryImpl(EntityManager em) {
        this.em = em;
    }


    @Override
    public Page<Product> searchForProduct(String searchWord, int page, int size, ProductSortType productSortType) {
        String[] searchWords = getSearchWords(searchWord);
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Product.class)
                .get();
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        for (String search : searchWords) {
            Query query = queryBuilder.keyword()
                    .onFields(NAME_FIELD, BRAND_FIELD, CATEGORY_FIELD)
                    .matching(search)
                    .createQuery();
            booleanQueryBuilder.add(query, BooleanClause.Occur.MUST);
        }
        Query isAvailableQuery = queryBuilder.keyword()
                .onField(IS_AVAILABLE_FIELD)
                .matching(true)
                .createQuery();

        booleanQueryBuilder.add(isAvailableQuery, BooleanClause.Occur.MUST);
        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(booleanQueryBuilder.build());
        Sort sort;
        if (productSortType.getSortType().equals(DESCENDING_TYPE)) {
            sort = new Sort(SortField.FIELD_SCORE,
                    new SortField(productSortType.getField(), productSortType.getSortFieldType(), true));
        } else {
            sort = new Sort(SortField.FIELD_SCORE,
                    new SortField(productSortType.getField(), productSortType.getSortFieldType()));
        }
        jpaQuery.setSort(sort);
        long totalElements = jpaQuery.getResultSize();
        jpaQuery.setFirstResult(page * size);
        if (size != 0) {
            jpaQuery.setMaxResults(size);
        }
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
