package com.shoppingmall.product.controller;

import com.shoppingmall.product.dto.ProductDto;
import com.shoppingmall.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/test")
    public ResponseEntity<Boolean> login() {
        LOGGER.debug("Login success");
        return ResponseEntity.ok(true);
    }

    @PostMapping("/add/product")
    public ResponseEntity<Boolean> addProduct(@ModelAttribute ProductDto productDto) throws IOException {
        LOGGER.debug("image: " + productDto.getImage().getOriginalFilename());
        Boolean isProductSaved = productService.addProduct(productDto);
        if (isProductSaved) {
            Boolean isUploaded = productService.uploadProductImage(productDto.getImage());
            return ResponseEntity.ok(isUploaded);
        }
        return ResponseEntity.ok(false);
    }

}
