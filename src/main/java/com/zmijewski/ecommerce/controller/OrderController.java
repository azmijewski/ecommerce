package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.dto.CreatedOrderDTO;
import com.zmijewski.ecommerce.dto.OrderDTO;
import com.zmijewski.ecommerce.dto.ShortOrderDTO;
import com.zmijewski.ecommerce.model.enums.OrderStatus;
import com.zmijewski.ecommerce.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/")
@Log4j2
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("orders")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Page<ShortOrderDTO>> getOrders(@RequestParam("page") @Min(0) int page,
                                                         @RequestParam("size") @Min(1) int size) {
        log.info("Getting orders page: {}, size: {}", page, size);
        Page<ShortOrderDTO> result = orderService.getOrders(page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("ordersByUser")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Page<ShortOrderDTO>> getOrdersByUser(Principal principal,
                                                               @RequestParam("page") @Min(0) int page,
                                                               @RequestParam("size") @Min(1) int size) {
        log.info("Getting orders page: {}, size: {} for user: {}", page, size, principal.getName());
        Page<ShortOrderDTO> result = orderService.getOrdersForUser(page, size, principal.getName());
        return ResponseEntity.ok(result);
    }

    @GetMapping("orders/{orderId}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable(name = "orderId") Long orderId) {
        log.info("Getting orderd with id: {}", orderId);
        OrderDTO result = orderService.getOrderById(orderId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("orders")
    @Secured({"ROLE_USER"})
    public ResponseEntity<CreatedOrderDTO> createOrder(Principal principal,
                                                       @RequestParam(name = "cartId") Long cartId,
                                                       @RequestParam(name = "shippingId") Long shippingId,
                                                       @RequestParam(name = "paymentId") Long paymentId) {
        log.info("Creating order with paymentId: {}, shippingId: {} for user: {}", paymentId, shippingId, principal.getName());
        CreatedOrderDTO result = orderService.createOrder(principal.getName(), cartId, shippingId, paymentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("orders/{orderId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> changeOrderStatus(@PathVariable(name = "orderId") Long orderId,
                                               @RequestParam(name = "status") OrderStatus status) {
        log.info("Changing status of order with id: {} to status: {}", orderId, status);
        orderService.changeOrderStatus(orderId, status);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("orders/{orderId}/paymentLink")
    @Secured({"ROLE_USER"})
    public ResponseEntity<?> getPaymentLink(@PathVariable(name = "orderId") Long orderId){
        log.info("Getting payment link for order with id: {}", orderId);
        String location = orderService.getPaymentLink(orderId);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header("location", location).build();
    }
    @PutMapping("orders")
    @Secured({"ROLE_USER"})
    public ResponseEntity<?> executePayment(@RequestParam(name = "payerId") String payerId,
                                            @RequestParam(name = "paymentId") String paymentId) {
        log.info("Executing payment with payerId: {}, paymentId: {}", payerId, paymentId);
        orderService.executePayment(paymentId, payerId);
        return ResponseEntity.noContent().build();
    }
}
