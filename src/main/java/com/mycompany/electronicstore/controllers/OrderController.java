package com.mycompany.electronicstore.controllers;

import com.mycompany.electronicstore.dtos.ApiResponseMessage;
import com.mycompany.electronicstore.dtos.OrderDto;
import com.mycompany.electronicstore.dtos.OrderRequest;
import com.mycompany.electronicstore.dtos.PageableResponse;
import com.mycompany.electronicstore.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderRequest orderRequest){
        OrderDto orderDto = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable("orderId") String orderId){
        orderService.removeOrder(orderId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .httpStatus(HttpStatus.OK)
                .message("Order is removed")
                .success(true)
                .build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable("userId") String userId){
        List<OrderDto> orderDtos = orderService.getOrders(userId);
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "orderedDate",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<OrderDto> pageableResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }
}
