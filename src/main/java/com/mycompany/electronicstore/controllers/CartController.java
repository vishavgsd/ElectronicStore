package com.mycompany.electronicstore.controllers;

import com.mycompany.electronicstore.dtos.AddItemToCartRequest;
import com.mycompany.electronicstore.dtos.ApiResponseMessage;
import com.mycompany.electronicstore.dtos.CartDto;
import com.mycompany.electronicstore.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable ("userId") String userId, @RequestBody AddItemToCartRequest request) {
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable ("userId") String userId, @PathVariable("cartItemId") int cartItemId) {
        cartService.removeItemFromCart(userId, cartItemId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Item is removed")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable("userId") String userId) {
        cartService.clearCart(userId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Cart is cleared")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable ("userId") String userId) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
}
