package com.shoppingmall.product.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    Boolean addProduct();

    String uploadProductImage(MultipartFile file) throws IOException;
}
