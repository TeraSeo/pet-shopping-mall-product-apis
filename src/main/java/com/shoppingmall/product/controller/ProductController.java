package com.shoppingmall.product.controller;

import com.shoppingmall.product.dto.ProductDto;
import com.shoppingmall.product.dto.ProductDtoWithoutImage;
import com.shoppingmall.product.entity.Category;
import com.shoppingmall.product.entity.Product;
import com.shoppingmall.product.entity.ProductDetail;
import com.shoppingmall.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

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

    @GetMapping("/get/products")
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<Product> products = productService.getAllProducts();
        products.stream().forEach(
            product -> {
                Set<ProductDetail> productDetails = product.getProductDetails();
                productDetails.stream().forEach(productDetail -> {
                    ProductDto productDto = ProductDto.builder()
                            .id(productDetail.getId())
                            .name(productDetail.getName())
                            .price(productDetail.getPrice())
                            .deliveryFee(productDetail.getDeliveryFee())
                            .user_id(String.valueOf(productDetail.getUser().getId()))
                            .quantity(productDetail.getQuantity())
                            .category(product.getCategory())
                            .subCategory(productDetail.getSubCategory())
                            .imagePath(productDetail.getImage())
                            .build();
                    productDtoList.add(productDto);
                });
            }
        );
        return ResponseEntity.ok().body(productDtoList);
    }

    @PostMapping("/add/product")
    public ResponseEntity<Boolean> addProduct(@ModelAttribute ProductDto productDto) throws IOException {
        LOGGER.debug("add product");
        Random random = new Random();
        String fileName = productDto.getImage().getOriginalFilename() + random.nextInt(100000);
        Boolean isProductSaved = productService.addProduct(productDto, fileName);
        if (isProductSaved) {
            Boolean isUploaded = productService.uploadProductImage(productDto.getImage(), fileName);
            return ResponseEntity.ok(isUploaded);
        }
        return ResponseEntity.ok(false);
    }

    @PutMapping("/edit/product")
    public ResponseEntity<Boolean> editProduct(@ModelAttribute ProductDto productDto) throws IOException {
        Boolean isEdited = productService.editProduct(productDto);
        return ResponseEntity.ok(isEdited);
    }

    @PutMapping("/edit/product/without/image")
    public ResponseEntity<Boolean> editProductWithoutImage(@RequestBody ProductDtoWithoutImage productDto) {
        Boolean isEdited = productService.editProductWithoutImage(productDto);
        return ResponseEntity.ok(isEdited);
    }

    @DeleteMapping("/delete/products")
    public ResponseEntity<Boolean> deleteProducts(@RequestHeader List<String> productIds, @RequestHeader List<String> imagePaths) throws IOException {
        LOGGER.debug("delete products");
        Boolean isDeleted = productService.deleteProducts(productIds, imagePaths);
        return ResponseEntity.ok(isDeleted);
    }

    @GetMapping("/get/by/sub/category")
    public ResponseEntity<List<ProductDto>> getProductsBySubCategory(@RequestParam String category, @RequestParam String subCategory) {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<ProductDetail> productDetails = productService.findProductsBySubCategory(subCategory);
        productDetails.stream().forEach(
                productDetail -> {
                    ProductDto productDto = ProductDto.builder()
                            .id(productDetail.getId())
                            .name(productDetail.getName())
                            .price(productDetail.getPrice())
                            .deliveryFee(productDetail.getDeliveryFee())
                            .user_id(String.valueOf(productDetail.getUser().getId()))
                            .quantity(productDetail.getQuantity())
                            .category(Category.valueOf(category))
                            .subCategory(productDetail.getSubCategory())
                            .imagePath(productDetail.getImage())
                            .build();

                    productDtoList.add(productDto);
                }
        );
        return ResponseEntity.ok(productDtoList);
    }

}
