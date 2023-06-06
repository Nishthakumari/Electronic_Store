package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.*;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public OrderDto createOrder(OrderDto orderDto, String userId, String cartId) {

        //fetch user
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found"));

        //fetch cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new ResourceNotFoundException("cart not found"));

        List<CartItem> cartItems = cart.getItems();

        if(cartItems.size()<=0) {
            throw new BadApiRequestException("Invalid number of item in the cart");

        }
        //other checks

        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingAddress(orderDto.getBillingAddress())
                .billingPhone(orderDto.getBillingPhone())
                .orderedDate(orderDto.getOrderedDate())
                .deliveredDate(orderDto.getDeliveredDate())
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getPaymentStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        //orderItem, amount

            AtomicReference<Integer> orderAmount = new AtomicReference<>();

            List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {

        //cartItem-> orderItem
            OrderItem orderItem = OrderItem.builder()
               .quantity(cartItem.getQuantity())
               .product(cartItem.getProduct())
               .totalPrice(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice())
               .order(order)
               .build();

            orderAmount.set(orderAmount.get()+ orderItem.getTotalPrice());
        return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        //cart clear
            cart.getItems().clear();
            cartRepository.save(cart);
            Order savedOrder = orderRepository.save(order);



         return mapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {

    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        return null;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }
}
