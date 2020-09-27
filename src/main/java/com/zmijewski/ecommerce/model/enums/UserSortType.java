package com.zmijewski.ecommerce.model.enums;

import lombok.Getter;
import org.apache.lucene.search.SortField;
@Getter
public enum UserSortType {
    ID_ASC("id", "asc", SortField.Type.LONG),
    ID_DESC("id", "desc", SortField.Type.LONG),
    FIRSTNAME_ASC("firstName", "asc", SortField.Type.STRING),
    FIRSTNAME_DESC("firstName", "desc", SortField.Type.STRING),
    LASTNAME_ASC("lastName", "asc", SortField.Type.STRING),
    LASTNAME_DESC("lastName", "desc", SortField.Type.STRING),
    EMAIL_ASC("email", "asc", SortField.Type.STRING),
    EMAIL_DESC("email", "desc", SortField.Type.STRING);

    String field;
    String sortType;
    SortField.Type sortFieldType;

    UserSortType(String field, String sortType, SortField.Type sortFieldType) {
        this.field = field;
        this.sortType = sortType;
        this.sortFieldType = sortFieldType;
    }

}
