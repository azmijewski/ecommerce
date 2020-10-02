package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.CreatedOrderDTO;
import com.zmijewski.ecommerce.dto.EmailDTO;
import com.zmijewski.ecommerce.dto.OrderDTO;
import com.zmijewski.ecommerce.dto.ShortOrderDTO;
import com.zmijewski.ecommerce.exception.OrderNotFoundException;
import com.zmijewski.ecommerce.exception.PaymentTypeNotEqualsException;
import com.zmijewski.ecommerce.mapper.OrderMapper;
import com.zmijewski.ecommerce.model.entity.*;
import com.zmijewski.ecommerce.model.enums.OrderStatus;
import com.zmijewski.ecommerce.model.enums.PaymentType;
import com.zmijewski.ecommerce.payment.OnlinePaymentClient;
import com.zmijewski.ecommerce.payment.model.CompleteOrderResponse;
import com.zmijewski.ecommerce.payment.model.CreateOrderResponse;
import com.zmijewski.ecommerce.repository.*;
import com.zmijewski.ecommerce.service.impl.OrderServiceImpl;
import com.zmijewski.ecommerce.util.EmailTemplateCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    OrderServiceImpl orderService;
    @Mock
    OrderRepository orderRepository;
    @Mock
    PaymentRepository paymentRepository;
    @Mock
    ShippingRepository shippingRepository;
    @Mock
    CartRepository cartRepository;
    @Mock
    OnlinePaymentClient onlinePaymentClient;
    @Mock
    OrderMapper orderMapper;
    @Mock
    UserRepository userRepository;
    @Mock
    OrderProductRepository orderProductRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    RabbitTemplate rabbitTemplate;
    @Mock
    EmailTemplateCreator templateCreator;


    @Test
    void shouldGetOrders() {
        //given
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(new Order())));
        when(orderMapper.mapToShortOrderDTO(any())).thenReturn(new ShortOrderDTO());
        //when
        Page<ShortOrderDTO> result = orderService.getOrders(0, 10);
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldGetOrdersForUser() {
        //given
        when(orderRepository.getOrderByEmail(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(new Order())));
        when(orderMapper.mapToShortOrderDTO(any())).thenReturn(new ShortOrderDTO());
        //when
        Page<ShortOrderDTO> result = orderService.getOrdersForUser(0, 10, "test@test");
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldGetOrderById() {
        //given
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(new Order()));
        when(orderMapper.mapToDTO(any())).thenReturn(new OrderDTO());
        //when
        OrderDTO result = orderService.getOrderById(1L);
        //then
        assertNotNull(result);
    }
    @Test
    void shouldNotGetOrderByIdIfNotExist() {
        //given
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when && then
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));
    }
    @Test
    void shouldCreatePostPaidOrder() {
        //given
        PaymentType paymentType = PaymentType.POSTPAID;
        Payment payment = Payment.builder()
                .paymentType(paymentType)
                .build();
        Shipping shipping = Shipping.builder()
                .paymentType(paymentType)
                .price(BigDecimal.TEN)
                .build();
        User user = new User();
        CartProduct cartProduct = CartProduct.builder()
                .product(new Product())
                .quantity(1)
                .build();
        Cart cart = Cart.builder()
                .cartProducts(Collections.singletonList(cartProduct))
                .totalPrice(BigDecimal.TEN)
                .build();
        Order order = new Order();
        order.setId(1L);
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(shippingRepository.findById(anyLong())).thenReturn(Optional.of(shipping));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(cartRepository.findWithProductsById(anyLong())).thenReturn(Optional.of(cart));
        when(orderRepository.save(any())).thenReturn(order);
        when(orderMapper.mapToCreatedOrderDTO(any())).thenReturn(new CreatedOrderDTO());
        when(orderProductRepository.save(any())).thenReturn(new OrderProduct());
        when(templateCreator.getOrderCreatedTemplate(anyLong())).thenReturn("test");
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));
        //when
        CreatedOrderDTO result = orderService.createOrder("test@test", 1L, 1L, 1L);
        //then
        assertNotNull(result);
    }
    @Test
    void shouldCreatePrePaidOrder() {
        //given
        PaymentType paymentType = PaymentType.PREPAID;
        Payment payment = Payment.builder()
                .paymentType(paymentType)
                .build();
        Shipping shipping = Shipping.builder()
                .paymentType(paymentType)
                .price(BigDecimal.TEN)
                .build();
        User user = new User();
        CartProduct cartProduct = CartProduct.builder()
                .product(new Product())
                .quantity(1)
                .build();
        Cart cart = Cart.builder()
                .cartProducts(Collections.singletonList(cartProduct))
                .totalPrice(BigDecimal.TEN)
                .build();
        Order order = new Order();
        order.setId(1L);
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(shippingRepository.findById(anyLong())).thenReturn(Optional.of(shipping));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(cartRepository.findWithProductsById(anyLong())).thenReturn(Optional.of(cart));
        when(orderRepository.save(any())).thenReturn(order);
        when(orderMapper.mapToCreatedOrderDTO(any())).thenReturn(new CreatedOrderDTO());
        when(orderProductRepository.save(any())).thenReturn(new OrderProduct());
        when(templateCreator.getOrderCreatedTemplate(anyLong())).thenReturn("test");
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));
        when(onlinePaymentClient.createPayment(any())).thenReturn(new CreateOrderResponse());
        //when
        CreatedOrderDTO result = orderService.createOrder("test@test", 1L, 1L, 1L);
        //then
        assertNotNull(result);
    }
    @Test
    void shouldNotCreateOrderIfPaymentTypesNotEquals() {
        //given
        Payment payment = Payment.builder()
                .paymentType(PaymentType.POSTPAID)
                .build();
        Shipping shipping = Shipping.builder()
                .paymentType(PaymentType.PREPAID)
                .price(BigDecimal.TEN)
                .build();
        User user = new User();
        CartProduct cartProduct = CartProduct.builder()
                .product(new Product())
                .quantity(1)
                .build();
        Cart cart = Cart.builder()
                .cartProducts(Collections.singletonList(cartProduct))
                .totalPrice(BigDecimal.TEN)
                .build();
        Order order = new Order();
        order.setId(1L);
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(shippingRepository.findById(anyLong())).thenReturn(Optional.of(shipping));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(cartRepository.findWithProductsById(anyLong())).thenReturn(Optional.of(cart));
        //when && then
        assertThrows(PaymentTypeNotEqualsException.class,
                () -> orderService.createOrder("test@test", 1L, 1L, 1L));
    }
    @Test
    void shouldChangeOrderStatusIfNotCotReturnedOrCancelled() {
        //given
        User user = User.builder()
                .email("test@test")
                .build();
        Order order = Order.builder()
                .id(1L)
                .user(user)
                .build();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);
        when(templateCreator.getOrderStatusChangedTemplate(anyLong(), any())).thenReturn("test");
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));
        //when
        orderService.changeOrderStatus(1L, OrderStatus.IN_DELIVERY);
        //then
        verify(orderRepository).save(any());
        verify(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));
    }
    @Test
    void shouldChangeOrderStatusWithCleanProductIfStatusIdCancelledOrReturned() {
        //given
        User user = User.builder()
                .email("test@test")
                .build();
        Product product = Product.builder()
                .id(1L)
                .build();
        OrderProduct orderProduct = OrderProduct.builder()
                .product(product)
                .quantity(1)
                .build();
        Order order = Order.builder()
                .id(1L)
                .user(user)
                .orderProducts(Collections.singletonList(orderProduct))
                .build();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);
        when(templateCreator.getOrderStatusChangedTemplate(anyLong(), any())).thenReturn("test");
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));
        doNothing().when(productRepository).increaseProductQuantity(anyLong(), anyInt());
        doNothing().when(orderProductRepository).delete(any());
        //when
        orderService.changeOrderStatus(1L, OrderStatus.CANCELLED);
        //then
        verify(orderRepository).save(any());
        verify(rabbitTemplate).convertAndSend(anyString(), any(EmailDTO.class));
        verify(productRepository).increaseProductQuantity(anyLong(), anyInt());
        verify(orderProductRepository).delete(any());
    }
    @Test
    void shouldGetPaymentLink() {
        //given
        String expectedLink = "http://localhost:4200/payment/link";
        User user = User.builder()
                .email("test@test")
                .build();
        Order order = Order.builder()
                .id(1L)
                .user(user)
                .build();
        CreateOrderResponse response = CreateOrderResponse.builder()
                .redirectUrl(expectedLink)
                .build();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(onlinePaymentClient.createPayment(any())).thenReturn(response);
        when(orderRepository.save(any())).thenReturn(order);
        //when
        String result = orderService.getPaymentLink(1L);
        //then
        assertEquals(expectedLink, result);
    }
    @Test
    void shouldExecutePayment() {
        //given
        when(orderRepository.findByPaymentToken(anyString())).thenReturn(Optional.of(new Order()));
        when(onlinePaymentClient.completeOrder(any())).thenReturn(new CompleteOrderResponse("test"));
        when(orderRepository.save(any())).thenReturn(new Order());
        //when
        orderService.executePayment("test", "test");
        //then
        verify(onlinePaymentClient).completeOrder(any());
        verify(orderRepository).save(any());
    }

}