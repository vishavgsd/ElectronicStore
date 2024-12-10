package com.mycompany.electronicstore.services.impl;

import com.mycompany.electronicstore.dtos.OrderDto;
import com.mycompany.electronicstore.dtos.OrderRequest;
import com.mycompany.electronicstore.dtos.PageableResponse;
import com.mycompany.electronicstore.entities.*;
import com.mycompany.electronicstore.exceptions.BadApiRequestException;
import com.mycompany.electronicstore.exceptions.ResourceNotFoundException;
import com.mycompany.electronicstore.helper.Helper;
import com.mycompany.electronicstore.repositories.CartRepository;
import com.mycompany.electronicstore.repositories.OrderRepository;
import com.mycompany.electronicstore.repositories.UserRepository;
import com.mycompany.electronicstore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
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
    private ModelMapper modelMapper;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public OrderDto createOrder(OrderRequest orderRequest) {
        String userId = orderRequest.getUserId();
        String cartId = orderRequest.getCartId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!!"));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart with given id not found !!!"));
        List<CartItem> cartItemsList = cart.getCartItems();
        if(cartItemsList.size() <= 0){
            throw new BadApiRequestException("Invalid number of items in cart !!!");
        }
        Order order = Order.builder()
                .billingName(orderRequest.getBillingName())
                .billingPhone(orderRequest.getBillingPhone())
                .billingAddress(orderRequest.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderRequest.getPaymentStatus())
                .orderStatus(orderRequest.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);

        List<OrderItem> orderItemsList = cartItemsList.stream().map(cartItem -> {
        //CartItem -> OrderItem
        OrderItem orderItem = OrderItem.builder()
                .quantity(cartItem.getQuantity())
                .product(cartItem.getProduct())
                .totalPrice(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice())
                .order(order)
                .build();
        orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
        return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItemsList);
        order.setOrderAmount(orderAmount.get());

        cart.getCartItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order with the given oreder id is not found !!!"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrders(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found !!!"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderRepository.findAll(pageable);
        PageableResponse<OrderDto> pageableResponse = Helper.getPageableResponse(page, OrderDto.class);
        return pageableResponse;
    }
}
