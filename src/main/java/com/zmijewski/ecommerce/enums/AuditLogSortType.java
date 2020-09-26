package com.zmijewski.ecommerce.enums;

import lombok.Getter;

@Getter
public enum AuditLogSortType {

    ID_ASC("id", "asc"),
    ID_DESC("id", "desc"),
    DATE_ASC("createdAt", "asc"),
    DATE_DESC("createdAt", "desc");

    String field;
    String sortType;

    AuditLogSortType(String field, String sortType) {
        this.field = field;
        this.sortType = sortType;
    }
}
