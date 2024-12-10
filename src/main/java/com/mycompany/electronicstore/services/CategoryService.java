package com.mycompany.electronicstore.services;

import com.mycompany.electronicstore.dtos.CategoryDto;
import com.mycompany.electronicstore.dtos.PageableResponse;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto CategoryDto);
    CategoryDto updateCategory(String categoryId, CategoryDto categoryDto);
    void deleteCategory(String categoryId);
    PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);
    CategoryDto getCategory(String categoryId);
}
