package com.zmijewski.ecommerce.model;

public interface Auditable {
    String getInsertMessage();
    String getUpdateMessage();
    String getDeleteMessage();
}
