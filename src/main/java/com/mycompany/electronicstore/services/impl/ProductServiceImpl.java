package com.mycompany.electronicstore.services.impl;

import com.mycompany.electronicstore.controllers.ProductController;
import com.mycompany.electronicstore.dtos.PageableResponse;
import com.mycompany.electronicstore.dtos.ProductDto;
import com.mycompany.electronicstore.entities.Category;
import com.mycompany.electronicstore.entities.Product;
import com.mycompany.electronicstore.exceptions.ResourceNotFoundException;
import com.mycompany.electronicstore.helper.Helper;
import com.mycompany.electronicstore.repositories.CategoryRepository;
import com.mycompany.electronicstore.repositories.ProductRepository;
import com.mycompany.electronicstore.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${product.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(ProductController.class);


    @Override
    public ProductDto createProduct(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        System.out.println(productDto.toString());

        Product product = modelMapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        ProductDto savedProductDto = modelMapper.map(savedProduct, ProductDto.class);
        System.out.println(savedProduct.toString());
        System.out.println(savedProductDto.toString());
        return savedProductDto;
    }

    @Override
    public ProductDto updateProduct(String productId, ProductDto productDto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found !!"));
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());
        Product savedProduct = productRepository.save(product);
        ProductDto updatedProductDto = modelMapper.map(savedProduct, ProductDto.class);
        return updatedProductDto;
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found !!"));
        String fullPath = imagePath + product.getProductImageName();
        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException exception){
            logger.info("Product image not found in folder");
            exception.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        productRepository.delete(product);
    }

    @Override
    public ProductDto getProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found !!"));
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return productDto;
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findAll(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> getLiveProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByTitleContaining(subTitle, pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found !!"));

        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);
        productDto.setAddedDate(new Date());
        Product product = modelMapper.map(productDto, Product.class);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        ProductDto savedProductDto = modelMapper.map(savedProduct, ProductDto.class);
        return savedProductDto;
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found !!"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found !!"));
        product.setCategory(category);
        Product updatedProduct = productRepository.save(product);
        ProductDto updatedProductDto = modelMapper.map(updatedProduct, ProductDto.class);
        return updatedProductDto;
    }

    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found !!"));
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByCategory(category, pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
        return pageableResponse;
    }
}
