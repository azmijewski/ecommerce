package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.CreatedOrderDTO;
import com.zmijewski.ecommerce.dto.OrderDTO;
import com.zmijewski.ecommerce.dto.ShortOrderDTO;
import com.zmijewski.ecommerce.model.entity.Order;
import com.zmijewski.ecommerce.model.enums.OrderStatus;
import org.springframework.data.domain.Page;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public interface OrderService {
    Page<ShortOrderDTO> getOrders(int page, int size);
    Page<ShortOrderDTO> getOrdersForUser(int page, int size, String email);
    OrderDTO getOrderById(Long orderId);
    CreatedOrderDTO createOrder(String email, Long cartId, Long shippingId, Long paymentId);
    void changeOrderStatus(Long orderId, OrderStatus orderStatus);
    String getPaymentLink(Long orderId);
    void executePayment(String paymentId, String payerId);
    void cancelOrdersWaitingForExpire();
    void sendRemindPaymentNotifications();
}
