package com.mycompany.electronicstore.config;

import com.mycompany.electronicstore.dtos.CartDto;
import com.mycompany.electronicstore.dtos.CartItemDto;
import com.mycompany.electronicstore.dtos.CategoryDto;
import com.mycompany.electronicstore.dtos.ProductDto;
import com.mycompany.electronicstore.entities.Cart;
import com.mycompany.electronicstore.entities.CartItem;
import com.mycompany.electronicstore.entities.Category;
import com.mycompany.electronicstore.entities.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
//        return modelMapper;
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT) // Ensures precise matching
                .setSkipNullEnabled(true); // Prevents null values from overwriting existing values

        // Custom mapping for Product to ProductDto
        modelMapper.typeMap(Product.class, ProductDto.class)
                .addMappings(mapper -> mapper.map(src -> src.getCategory(), ProductDto::setCategoryDto));

        // Custom mapping for ProductDto to Product
        modelMapper.typeMap(ProductDto.class, Product.class)
                .addMappings(mapper -> mapper.map(src -> src.getCategoryDto(), Product::setCategory));

        // Custom mapping for Cart to CartDto
        modelMapper.typeMap(Cart.class, CartDto.class)
                .addMappings(mapper -> {
                    mapper.map(Cart::getUser, CartDto::setUserDto);
                    mapper.map(Cart::getCartItems, CartDto::setCartItemsDtoList);
                });

        // Custom mapping for CartDto to Cart
        modelMapper.typeMap(CartDto.class, Cart.class)
                .addMappings(mapper -> {
                    mapper.map(CartDto::getUserDto, Cart::setUser);
                    mapper.map(CartDto::getCartItemsDtoList, Cart::setCartItems);
                });

        // Custom mapping for CartItem to CartItemDto
        modelMapper.typeMap(CartItem.class, CartItemDto.class)
                .addMappings(mapper -> mapper.map(CartItem::getProduct, CartItemDto::setProductDto));

        // Custom mapping for CartItemDto to CartItem
        modelMapper.typeMap(CartItemDto.class, CartItem.class)
                .addMappings(mapper -> mapper.map(CartItemDto::getProductDto, CartItem::setProduct));
        // Additional mappings can be added as needed
        return modelMapper;
    }
}
