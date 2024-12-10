package com.mycompany.electronicstore.services;

import com.mycompany.electronicstore.dtos.PageableResponse;
import com.mycompany.electronicstore.dtos.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(String productId, ProductDto productDto);
    void deleteProduct(String productId);
    ProductDto getProduct(String productId);
    PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);
    PageableResponse<ProductDto> getLiveProducts(int pageNumber, int pageSize, String sortBy, String sortDir);
    PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);
    ProductDto createWithCategory(ProductDto productDto, String categoryId);
    ProductDto updateCategory(String productId, String categoryId);
    PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
}
