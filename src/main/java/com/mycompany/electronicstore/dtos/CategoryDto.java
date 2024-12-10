package com.mycompany.electronicstore.dtos;

import com.mycompany.electronicstore.entities.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private String categoryId;
    @NotBlank
    @Size(min = 4, message = "Title must be of minimum 4 characters")
    private String title;
    @NotBlank(message = "Description required !!")
    private String description;
    @NotBlank(message = "Cover Image is mandatory !!")
    private String coverImage;

  //  private List<ProductDto> productDtoList = new ArrayList<>();
}
