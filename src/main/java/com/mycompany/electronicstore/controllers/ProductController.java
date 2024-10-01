package com.mycompany.electronicstore.controllers;

import com.mycompany.electronicstore.dtos.*;
import com.mycompany.electronicstore.services.FileService;
import com.mycompany.electronicstore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        System.out.println("From controller");
        System.out.println(productDto.toString());
        ProductDto createdProductDto = productService.createProduct(productDto);
        return new ResponseEntity<>(createdProductDto, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("productId") String productId, @RequestBody ProductDto productDto) {
        ProductDto updtedProductDto = productService.updateProduct(productId, productDto);
        return new ResponseEntity<>(updtedProductDto, HttpStatus.OK);
    }

    @DeleteMapping("/{productID}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable("productID") String productId) {
        productService.deleteProduct(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Product deleted successfully").success(true).httpStatus(HttpStatus.OK).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> pageableResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("productId") String productId) {
        ProductDto productDto = productService.getProduct(productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getLiveProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> pageableResponse = productService.getLiveProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    @GetMapping("/search/{subTitle}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
            @PathVariable("subTitle") String subTitle,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> pageableResponse = productService.searchByTitle(subTitle, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable("productId") String productId,
            @RequestParam("productImage")MultipartFile image
            ) throws IOException {

        String fileName = fileService.uploadFile(image, imagePath);
        ProductDto productDto = productService.getProduct(productId);
        productDto.setProductImageName(fileName);
        ProductDto updatedProductDto = productService.updateProduct(productId, productDto);
        ImageResponse imageResponse = ImageResponse.builder().
                imageName(updatedProductDto.getProductImageName())
                .message("Product image is successfully uploaded !!")
                .httpStatus(HttpStatus.CREATED)
                .success(true)
                .build();

        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    @GetMapping("/image/{productId}")
    public void serveProductImage(@PathVariable("productId") String productId, HttpServletResponse httpServletResponse) throws IOException {

        ProductDto productDto = productService.getProduct(productId);
        logger.info("User Image Name : {} ", productDto.getProductImageName());
        InputStream inputStream = fileService.getResource(imagePath, productDto.getProductImageName());
        httpServletResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, httpServletResponse.getOutputStream());
    }

}
