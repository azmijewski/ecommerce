package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.CreatedOrderDTO;
import com.zmijewski.ecommerce.dto.EmailDTO;
import com.zmijewski.ecommerce.dto.OrderDTO;
import com.zmijewski.ecommerce.dto.ShortOrderDTO;
import com.zmijewski.ecommerce.exception.*;
import com.zmijewski.ecommerce.mapper.OrderMapper;
import com.zmijewski.ecommerce.model.entity.*;
import com.zmijewski.ecommerce.model.enums.GlobalParameterName;
import com.zmijewski.ecommerce.model.enums.OrderStatus;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import com.zmijewski.ecommerce.payment.OnlinePaymentClient;
import com.zmijewski.ecommerce.payment.model.CompleteOrderRequest;
import com.zmijewski.ecommerce.payment.model.CreateOrderRequest;
import com.zmijewski.ecommerce.payment.model.CreateOrderResponse;
import com.zmijewski.ecommerce.repository.*;
import com.zmijewski.ecommerce.service.OrderService;
import com.zmijewski.ecommerce.util.EmailTemplateCreator;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@PropertySource("classpath:subject.properties")
@Log4j2
public class OrderServiceImpl implements OrderService {

    private static final String EMAIL_QUEUE = "emailQueue";
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ShippingRepository shippingRepository;
    private final CartRepository cartRepository;
    private final OnlinePaymentClient onlinePaymentClient;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;
    private final EmailTemplateCreator templateCreator;
    private final GlobalParameterRepository globalParameterRepository;
    private final String orderCreatedSubject;
    private final String orderStatusChangedSubject;
    private final String orderNearExpiredSubject;


    public OrderServiceImpl(OrderRepository orderRepository,
                            PaymentRepository paymentRepository,
                            ShippingRepository shippingRepository,
                            CartRepository cartRepository, OnlinePaymentClient onlinePaymentClient,
                            OrderMapper orderMapper, UserRepository userRepository,
                            OrderProductRepository orderProductRepository, ProductRepository productRepository,
                            RabbitTemplate rabbitTemplate, EmailTemplateCreator templateCreator,
                            GlobalParameterRepository globalParameterRepository,
                            @Value("${order-created-subject}") String orderCreatedSubject,
                            @Value("${order-status-changed}") String orderStatusChangedSubject,
                            @Value("${order-near-expired}")String orderNearExpiredSubject) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.shippingRepository = shippingRepository;
        this.cartRepository = cartRepository;
        this.onlinePaymentClient = onlinePaymentClient;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.templateCreator = templateCreator;
        this.orderCreatedSubject = orderCreatedSubject;
        this.orderStatusChangedSubject = orderStatusChangedSubject;
        this.globalParameterRepository = globalParameterRepository;
        this.orderNearExpiredSubject = orderNearExpiredSubject;
    }


    @Override
    public Page<ShortOrderDTO> getOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable)
                .map(orderMapper::mapToShortOrderDTO);
    }

    @Override
    public Page<ShortOrderDTO> getOrdersForUser(int page, int size, String email) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.getOrderByEmail(email, pageable)
                .map(orderMapper::mapToShortOrderDTO);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Could not find order with id:" + orderId));
        return orderMapper.mapToDTO(order);
    }

    @Override
    @Transactional
    public CreatedOrderDTO createOrder(String email, Long cartId, Long shippingId, Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Could not find payment with id: " + paymentId));
        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new ShippingNotFoundException("Could not find shipping with id: " + shippingId));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
        Cart cart = cartRepository.findWithProductsById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Could not find cart with id: " + cartId));
        PaymentType paymentType = payment.getPaymentType();
        if (!paymentType.equals(shipping.getPaymentType())) {
            throw new PaymentTypeNotEqualsException("Payment type for payment with id: " + paymentId +
                    " is not equals to shipping with id: " + shippingId);
        }
        Order orderToSave = Order.builder()
                .payment(payment)
                .shipping(shipping)
                .user(user)
                .build();
        BigDecimal totalPrice = cart.getTotalPrice().add(shipping.getPrice());
        orderToSave.setTotalPrice(totalPrice);
        String paymentUri = null;
        boolean payNow = false;
        if (PaymentType.PREPAID.equals(paymentType)) {
            CreateOrderRequest orderRequest = CreateOrderRequest.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .totalAmount(totalPrice)
                    .build();
            CreateOrderResponse response = onlinePaymentClient.createPayment(orderRequest);
            orderToSave.setPaymentToken(response.getOrderId());
            paymentUri = response.getRedirectUrl();
            payNow = true;
            orderToSave.setOrderStatus(OrderStatus.WAITING_FOR_PAYMENT);
        } else {
            orderToSave.setOrderStatus(OrderStatus.CREATED);
        }
        Order savedOrder = orderRepository.save(orderToSave);
        addProductToOrder(savedOrder, cart.getCartProducts());
        CreatedOrderDTO createdOrderDTO = orderMapper.mapToCreatedOrderDTO(savedOrder);
        createdOrderDTO.setPayNow(payNow);
        createdOrderDTO.setPaymentLink(paymentUri);
        String template = templateCreator.getOrderCreatedTemplate(savedOrder.getId());
        addMailSendToQueue(user.getEmail(), orderCreatedSubject, template);
        return createdOrderDTO;
    }

    @Override
    @Transactional
    public void changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order orderToChangeStatus = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Could not find order with id:" + orderId));
        orderToChangeStatus.setOrderStatus(orderStatus);
        if(OrderStatus.CANCELLED.equals(orderStatus) || OrderStatus.RETURNED.equals(orderStatus)) {
            cleanOrderProducts(orderToChangeStatus);
        }
        orderRepository.save(orderToChangeStatus);
        String template = templateCreator.getOrderStatusChangedTemplate(orderId, orderStatus);
        addMailSendToQueue(orderToChangeStatus.getUser().getEmail(), orderStatusChangedSubject, template);
    }


    @Override
    public String getPaymentLink(Long orderId) {
        Order orderToGetPaymentLink = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Could not find order with id:" + orderId));
        User user = orderToGetPaymentLink.getUser();
        CreateOrderRequest orderRequest = CreateOrderRequest.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .totalAmount(orderToGetPaymentLink.getTotalPrice())
                .build();
        CreateOrderResponse response = onlinePaymentClient.createPayment(orderRequest);
        orderToGetPaymentLink.setOrderStatus(OrderStatus.WAITING_FOR_PAYMENT);
        orderToGetPaymentLink.setPaymentToken(response.getOrderId());
        orderRepository.save(orderToGetPaymentLink);
        return response.getRedirectUrl();
    }

    @Override
    public void executePayment(String paymentId, String payerId) {
        Order orderToExecutePayment = orderRepository.findByPaymentToken(paymentId)
                .orElseThrow(() -> new OrderNotFoundException("Could not find order with payment token:" + payerId));
        CompleteOrderRequest completeOrderRequest = CompleteOrderRequest.builder()
                .payerId(payerId)
                .paymentId(paymentId)
                .build();
        onlinePaymentClient.completeOrder(completeOrderRequest);
        orderToExecutePayment.setPaymentToken(null);
        orderToExecutePayment.setOrderStatus(OrderStatus.PAYED);
        orderRepository.save(orderToExecutePayment);
    }

    @Override
    public void cancelOrdersWaitingForExpire() {
        orderRepository.getExpiredOrdersIdWhereWaitingForPayment(new Date()).forEach(orderId -> {
            log.info("Order with id: {} is going to be cancelled", orderId);
            changeOrderStatus(orderId, OrderStatus.CANCELLED);
        });
    }

    @Override
    public void sendRemindPaymentNotifications() {
        Integer daysToExpire = globalParameterRepository.getValueAsInteger(GlobalParameterName.INFORM_USER_ORDER_EXPIRE_IN_DAYS);
        orderRepository.getOrdersToExpiredInDays(new Date(), daysToExpire).forEach(order -> {
            log.info("Send remind notification for order with id: {}", order.getId());
            String content = templateCreator.getOrderNearExpireTemplate(order.getId(), daysToExpire);
            addMailSendToQueue(order.getUser().getEmail(), orderNearExpiredSubject, content);
        });
    }


    private void cleanOrderProducts(Order order) {
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            productRepository.increaseProductQuantity(orderProduct.getProduct().getId(), orderProduct.getQuantity());
            orderProductRepository.delete(orderProduct);
        }
        order.setOrderProducts(new ArrayList<>());
    }
    private void addProductToOrder(Order order, List<CartProduct> cartProducts) {
        cartProducts.forEach(cartProduct -> {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(cartProduct.getProduct());
            orderProduct.setQuantity(cartProduct.getQuantity());
            orderProductRepository.save(orderProduct);
        });
    }
    private void addMailSendToQueue(String sendTo, String subject, String content) {
        EmailDTO email = new EmailDTO();
        email.setContent(content);
        email.setSendTo(sendTo);
        email.setSubject(subject);
        rabbitTemplate.convertAndSend(EMAIL_QUEUE, email);
    }
}
