package com.zmijewski.ecommerce.model.enums;

public enum OrderStatus {
    CREATED(GlobalParameterName.ORDER_STATUS_CREATED_MESSAGE),
    PAYED(GlobalParameterName.ORDER_STATUS_PAYED_MESSAGE),
    IN_DELIVERY(GlobalParameterName.ORDER_STATUS_IN_DELIVERY_MESSAGE),
    DELIVERED(GlobalParameterName.ORDER_STATUS_DELIVERED_MESSAGE),
    PROCESSING(GlobalParameterName.ORDER_STATUS_PROCESSING_MESSAGE),
    WAITING_FOR_PAYMENT(GlobalParameterName.ORDER_STATUS_WAITING_FOR_PAYMENT_MESSAGE),
    CANCELLED(GlobalParameterName.ORDER_STATUS_CANCELLED_MESSAGE),
    RETURNED(GlobalParameterName.ORDER_STATUS_RETURNED_MESSAGE);

    private final GlobalParameterName globalParameterName;

    OrderStatus(GlobalParameterName globalParameterName) {
        this.globalParameterName = globalParameterName;
    }

    public GlobalParameterName getGlobalParameterName() {
        return globalParameterName;
    }
}
