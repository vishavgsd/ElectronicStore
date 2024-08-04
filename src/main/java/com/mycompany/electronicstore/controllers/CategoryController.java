package com.mycompany.electronicstore.controllers;

import com.mycompany.electronicstore.dtos.ApiResponseMessage;
import com.mycompany.electronicstore.dtos.CategoryDto;
import com.mycompany.electronicstore.dtos.PageableResponse;
import com.mycompany.electronicstore.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {

        CategoryDto categoryDto1 = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable("categoryId") String categoryId,
           @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategoryDto = categoryService.updateCategory(categoryId, categoryDto);
        return new ResponseEntity<>(updatedCategoryDto, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable("categoryId") String categoryId) {
        categoryService.deleteCategory(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Category is deleted successfully")
                .httpStatus(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity <PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir


    ){
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("categoryId") String categoryId) {
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(categoryDto);
    }
}
