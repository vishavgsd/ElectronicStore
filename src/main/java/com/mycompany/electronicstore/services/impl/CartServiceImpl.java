package com.mycompany.electronicstore.services.impl;

import com.mycompany.electronicstore.dtos.AddItemToCartRequest;
import com.mycompany.electronicstore.dtos.CartDto;
import com.mycompany.electronicstore.entities.Cart;
import com.mycompany.electronicstore.entities.CartItem;
import com.mycompany.electronicstore.entities.Product;
import com.mycompany.electronicstore.entities.User;
import com.mycompany.electronicstore.exceptions.BadApiRequestException;
import com.mycompany.electronicstore.exceptions.ResourceNotFoundException;
import com.mycompany.electronicstore.repositories.CartItemRepository;
import com.mycompany.electronicstore.repositories.CartRepository;
import com.mycompany.electronicstore.repositories.ProductRepository;
import com.mycompany.electronicstore.repositories.UserRepository;
import com.mycompany.electronicstore.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        String productId = request.getProductId();
        int quantity = request.getQuantity();

        if(quantity <= 0)
            throw new BadApiRequestException("Quantity must be greater than 0");

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = null;
        try{
             cart = cartRepository.findByUser(user).get();
        }catch (NoSuchElementException e){
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> cartItems = cart.getCartItems();
        List<CartItem> updatedItems = cartItems.stream().map(item -> {

            if(item.getProduct().getProductId().equals(productId)){
                item.setQuantity(quantity);
                item.setTotalPrice(product.getDiscountedPrice() * quantity);
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());
        cart.setCartItems(updatedItems);

        if(!updated.get()){
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getCartItems().add(cartItem);
        }

        cart.setUser(user);
        Cart  updatedCart = cartRepository.save(cart);
        CartDto updatedCartDto = modelMapper.map(updatedCart, CartDto.class);
        return updatedCartDto;
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        // Remove the cart item with the specified cartItemId
        boolean removed = cart.getCartItems().removeIf(item -> item.getCartItemId() == cartItemId);

        // If no item was removed, it means the item was not found in the cart
        if (!removed) {
            throw new ResourceNotFoundException("Cart item not found");
        }

        // Save the updated cart back to the repository
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cart.getCartItems().clear();
        cartRepository.save(cart);

    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return modelMapper.map(cart, CartDto.class);
    }
}