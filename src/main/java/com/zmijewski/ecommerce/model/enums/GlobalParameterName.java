package com.zmijewski.ecommerce.model.enums;

import com.zmijewski.ecommerce.model.entity.GlobalParameter;

public enum GlobalParameterName {
    REGISTRATION_URL(null),
    REDIRECT_ONLINE_PAYMENT_URL(null),
    REDIRECT_CANCEL_ONLINE_PAYMENT_URL(null),
    CHECK_ORDER_URL(null),
    ORDER_STATUS_CREATED_MESSAGE(OrderStatus.CREATED),
    ORDER_STATUS_PAYED_MESSAGE(OrderStatus.PAYED),
    ORDER_STATUS_IN_DELIVERY_MESSAGE(OrderStatus.IN_DELIVERY),
    ORDER_STATUS_DELIVERED_MESSAGE(OrderStatus.DELIVERED),
    ORDER_STATUS_PROCESSING_MESSAGE(OrderStatus.PROCESSING),
    ORDER_STATUS_WAITING_FOR_PAYMENT_MESSAGE(OrderStatus.WAITING_FOR_PAYMENT),
    ORDER_STATUS_CANCELLED_MESSAGE(OrderStatus.CANCELLED),
    ORDER_STATUS_RETURNED_MESSAGE(OrderStatus.RETURNED),
    INFORM_USER_ORDER_EXPIRE_IN_DAYS(null);

    private final OrderStatus orderStatus;

    GlobalParameterName(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static GlobalParameterName getByOrderStatus(OrderStatus orderStatus) {
        for (GlobalParameterName globalParameterName : GlobalParameterName.values()) {
            if (orderStatus.equals(globalParameterName.orderStatus)) {
                return globalParameterName;
            }
        }
        throw new IllegalArgumentException("Global Parameter Name not implemented for Order Status: " + orderStatus);
    }
}
