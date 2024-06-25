package com.shoppingmall.product.service;

import com.shoppingmall.product.repository.ProductRepository;
import com.shoppingmall.product.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImplTest.class);

    @Test
    void uploadProductImage() throws IOException {
        MultipartFile file = new MockMultipartFile("image", "src/test/resources/play.png", "image/jpeg", "src/test/resources/appstore.png".getBytes());
        Boolean isUploaded = productService.uploadProductImage(file, "fileName");
        LOGGER.debug("is uploaded: " + isUploaded);
    }

    @Test
    void deleteProductImage() {
        productService.deleteProductImage("playstore.png");
    }


    @Test
    void deleteProducts() {
        Boolean isDeleted = productService.deleteProducts(List.of("11"), List.of("test1.png"));
        LOGGER.debug("users: " + userRepository.findById(18L).get().getProductDetails().size());
        LOGGER.debug("products1: " + productRepository.findById(9L).get().getProductDetails().size());
        LOGGER.debug("products2: " + productRepository.findById(10L).get().getProductDetails().size());
        assertEquals(isDeleted, true);
    }
}