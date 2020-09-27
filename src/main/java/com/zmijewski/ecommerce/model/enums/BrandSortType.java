package com.zmijewski.ecommerce.model.enums;

import lombok.Getter;

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
