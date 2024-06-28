package com.shoppingmall.product.service;

import com.shoppingmall.product.dto.ProductDto;
import com.shoppingmall.product.dto.ProductDtoWithoutImage;
import com.shoppingmall.product.entity.Product;
import com.shoppingmall.product.entity.ProductDetail;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    Boolean addProduct(ProductDto productDto, String fileName);

    Boolean uploadProductImage(MultipartFile image, String fileName) throws IOException;

    void deleteProductImage(String fileName);

    List<Product> getAllProducts();

    Boolean deleteProducts(List<String> productIds, List<String> imagePaths);

    Boolean editProduct(ProductDto productDto) throws IOException;
    Boolean editProductWithoutImage(ProductDtoWithoutImage productDto);
    List<ProductDetail> findProductsBySubCategory(String subCategory);
}
