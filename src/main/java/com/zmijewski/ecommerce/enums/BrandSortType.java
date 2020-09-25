package com.zmijewski.ecommerce.enums;

import lombok.Getter;
import org.apache.lucene.search.SortField;
@Getter
public enum BrandSortType {
    ID_ASC("id", "asc"),
    ID_DESC("id", "desc"),
    NAME_ASC("name", "asc"),
    NAME_DESC("name", "desc");

    String field;
    String sortType;

    BrandSortType(String field, String sortType) {
        this.field = field;
        this.sortType = sortType;
    }
}
