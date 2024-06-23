package com.shoppingmall.product.service;

import com.shoppingmall.product.dto.ProductDto;
import com.shoppingmall.product.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    Boolean addProduct(ProductDto productDto);

    Boolean uploadProductImage(MultipartFile image) throws IOException;

    List<Product> getAllProducts();
}
