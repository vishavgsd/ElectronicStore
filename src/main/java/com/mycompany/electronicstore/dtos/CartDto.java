package com.mycompany.electronicstore.dtos;


import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CartDto {

    private String cartId;
    private Date createdAt;

    private UserDto userDto;

    private List<CartItemDto> cartItemsDtoList = new ArrayList<>();
}
