package com.shoppingmall.product.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    private final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImplTest.class);

    @Test
    void uploadProductImage() throws IOException {
        MultipartFile file = new MockMultipartFile("image", "src/test/resources/play.png", "image/jpeg", "src/test/resources/appstore.png".getBytes());
        String fileUrl = productService.uploadProductImage(file);
        LOGGER.debug("file url: " + fileUrl);
    }
}