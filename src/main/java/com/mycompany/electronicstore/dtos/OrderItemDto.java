package com.mycompany.electronicstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemDto {

    private String orderItemId;
    private int quantity;
    private int totalPrice;
    private ProductDto productDto;
    //private OrderDto orderDto;

}
