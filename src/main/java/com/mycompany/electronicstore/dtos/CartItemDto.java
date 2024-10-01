package com.mycompany.electronicstore.dtos;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CartItemDto {

    private int cartItemId;
    private ProductDto productDto;
    private int quantity;
    private int totalPrice;

 //   private CartDto cartDto;
}
