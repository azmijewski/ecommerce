package com.zmijewski.ecommerce.specification;

import com.zmijewski.ecommerce.model.enums.ProductSearchCriteria;
import com.zmijewski.ecommerce.model.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class ProductSearchSpecification implements Specification<Product> {

    private static final String PRICE_FIELD = "price";
    private static final String BRAND_FIELD = "brand.name";
    private static final String CATEGORY_FIELD = "category.name";
    private static final String IS_AVAILABLE_FIELD = "isAvailable";


    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private String brand;
    private String category;

    public ProductSearchSpecification(Map<ProductSearchCriteria, String> searchCriteria) {
        if (Objects.nonNull(searchCriteria.get(ProductSearchCriteria.PRICE_FROM))) {
            this.priceFrom = new BigDecimal(searchCriteria.get(ProductSearchCriteria.PRICE_FROM));
        }
        if (Objects.nonNull(searchCriteria.get(ProductSearchCriteria.PRICE_TO))) {
            this.priceTo = new BigDecimal(searchCriteria.get(ProductSearchCriteria.PRICE_TO));
        }
        this.brand = searchCriteria.get(ProductSearchCriteria.BRAND);
        this.category = searchCriteria.get(ProductSearchCriteria.CATEGORY);
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.and(priceFromPredicate(root, criteriaBuilder),
                priceToPredicate(root, criteriaBuilder),
                brandPredicate(root, criteriaBuilder),
                categoryPredicate(root, criteriaBuilder),
                isAvailablePredicate(root, criteriaBuilder)
                );
    }
    private Predicate priceFromPredicate(Root<Product> root, CriteriaBuilder criteriaBuilder) {
        if (Objects.nonNull(priceFrom)) {
            return criteriaBuilder.greaterThanOrEqualTo(root.get(PRICE_FIELD), priceFrom);
        }
        return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
    private Predicate priceToPredicate(Root<Product> root, CriteriaBuilder criteriaBuilder) {
        if (Objects.nonNull(priceTo)) {
            return criteriaBuilder.lessThanOrEqualTo(root.get(PRICE_FIELD), priceTo);
        }
        return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
    private Predicate brandPredicate(Root<Product> root, CriteriaBuilder criteriaBuilder) {
        if (Objects.nonNull(brand)) {
            return criteriaBuilder.equal(root.get(BRAND_FIELD), brand);
        }
        return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
    private Predicate categoryPredicate(Root<Product> root, CriteriaBuilder criteriaBuilder) {
        if (Objects.nonNull(category)) {
            return criteriaBuilder.equal(root.get(CATEGORY_FIELD), category);
        }
        return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
    private Predicate isAvailablePredicate(Root<Product> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.isTrue(root.get(IS_AVAILABLE_FIELD));
    }

}
