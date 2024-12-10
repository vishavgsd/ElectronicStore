package com.mycompany.electronicstore.services;

import com.mycompany.electronicstore.dtos.AddItemToCartRequest;
import com.mycompany.electronicstore.dtos.CartDto;

public interface CartService {

    CartDto addItemToCart(String userId, AddItemToCartRequest request);
    void removeItemFromCart(String userId, int cartItemId);
    void clearCart(String userId);
    CartDto getCartByUser(String userId);
}
