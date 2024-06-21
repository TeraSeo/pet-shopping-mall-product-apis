package com.shoppingmall.product.service;

import com.shoppingmall.product.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    Boolean addProduct(ProductDto productDto);

    Boolean uploadProductImage(MultipartFile image) throws IOException;
}
