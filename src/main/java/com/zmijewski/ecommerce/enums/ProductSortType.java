package com.zmijewski.ecommerce.enums;

import lombok.Getter;
import org.apache.lucene.search.SortField;

@Getter
public enum ProductSortType {
    PRICE_ASC("price", "asc", SortField.Type.STRING),
    PRICE_DESC("price", "desc", SortField.Type.STRING),
    NAME_ASC("name", "asc", SortField.Type.STRING),
    NAME_DESC("name", "desc", SortField.Type.STRING);

    String field;
    String sortType;
    SortField.Type sortFieldType;

    ProductSortType(String field, String sortType, SortField.Type sortFieldType) {
        this.field = field;
        this.sortType = sortType;
        this.sortFieldType = sortFieldType;
    }
}
