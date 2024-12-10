package com.mycompany.electronicstore.services;

import com.mycompany.electronicstore.dtos.OrderDto;
import com.mycompany.electronicstore.dtos.OrderRequest;
import com.mycompany.electronicstore.dtos.PageableResponse;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderRequest orderRequest);
    void removeOrder(String orderId);
    List<OrderDto> getOrders(String userId);
    PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);


}
