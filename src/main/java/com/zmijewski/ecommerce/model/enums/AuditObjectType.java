package com.zmijewski.ecommerce.model.enums;

import lombok.Getter;

@Getter
public enum AuditObjectType {
    BRAND(GlobalParameterName.AUDIT_LOG_BRAND_PERSIST_MESSAGE, GlobalParameterName.AUDIT_LOG_BRAND_UPDATE_MESSAGE, GlobalParameterName.AUDIT_LOG_BRAND_DELETE_MESSAGE),
    CATEGORY(GlobalParameterName.AUDIT_LOG_CATEGORY_PERSIST_MESSAGE, GlobalParameterName.AUDIT_LOG_CATEGORY_UPDATE_MESSAGE, GlobalParameterName.AUDIT_LOG_CATEGORY_DELETE_MESSAGE),
    PAYMENT(GlobalParameterName.AUDIT_LOG_PAYMENT_PERSIST_MESSAGE, GlobalParameterName.AUDIT_LOG_PAYMENT_UPDATE_MESSAGE, GlobalParameterName.AUDIT_LOG_PAYMENT_DELETE_MESSAGE),
    PRODUCT(GlobalParameterName.AUDIT_LOG_PRODUCT_PERSIST_MESSAGE, GlobalParameterName.AUDIT_LOG_PRODUCT_UPDATE_MESSAGE, GlobalParameterName.AUDIT_LOG_PRODUCT_DELETE_MESSAGE),
    SHIPPING(GlobalParameterName.AUDIT_LOG_SHIPPING_PERSIST_MESSAGE, GlobalParameterName.AUDIT_LOG_SHIPPING_UPDATE_MESSAGE, GlobalParameterName.AUDIT_LOG_SHIPPING_DELETE_MESSAGE),
    USER(GlobalParameterName.AUDIT_LOG_USER_PERSIST_MESSAGE, GlobalParameterName.AUDIT_LOG_USER_UPDATE_MESSAGE, GlobalParameterName.AUDIT_LOG_USER_DELETE_MESSAGE);

    private GlobalParameterName globalParameterPersistMessage;
    private GlobalParameterName globalParameterUpdateMessage;
    private GlobalParameterName globalParameterDeleteMessage;

    AuditObjectType(GlobalParameterName globalParameterPersistMessage,
                    GlobalParameterName globalParameterUpdateMessage,
                    GlobalParameterName globalParameterDeleteMessage) {
        this.globalParameterPersistMessage = globalParameterPersistMessage;
        this.globalParameterUpdateMessage = globalParameterUpdateMessage;
        this.globalParameterDeleteMessage = globalParameterDeleteMessage;
    }
}
