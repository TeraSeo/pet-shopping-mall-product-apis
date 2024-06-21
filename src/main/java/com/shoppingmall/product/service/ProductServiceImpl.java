package com.shoppingmall.product.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.shoppingmall.product.dto.ProductDto;
import com.shoppingmall.product.entity.Product;
import com.shoppingmall.product.entity.ProductDetail;
import com.shoppingmall.product.repository.ProductDetailRepository;
import com.shoppingmall.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Value("${s3.bucket}")
    private String bucket;

    private final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final AmazonS3Client s3Client;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    @Autowired
    public ProductServiceImpl(AmazonS3Client s3Client, ProductRepository productRepository, ProductDetailRepository productDetailRepository) {
        this.s3Client = s3Client;
        this.productRepository = productRepository;
        this.productDetailRepository = productDetailRepository;
    }

    @Override
    public Boolean addProduct(ProductDto productDto) {
        Optional<Product> p = productRepository.findByCategory(productDto.getCategory());
        Product product;
        if (p.isPresent()) {
            product = p.get();
        }
        else {
            product = Product.builder()
                    .category(productDto.getCategory())
                    .build();

            productRepository.save(product);
        }

        LOGGER.debug("file name: " + productDto.getImage().getOriginalFilename());

        ProductDetail productDetail = ProductDetail.builder()
                .name(productDto.getName())
                .summary(productDto.getSummary())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .deliveryFee(productDto.getDeliveryFee())
                .image(productDto.getImage().getOriginalFilename())
                .subCategory(productDto.getSubCategory())
                .build();

        product.add(productDetail);

        productDetailRepository.save(productDetail);
        return true;
    }

    @Override
    public Boolean uploadProductImage(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        s3Client.putObject(bucket, originalFilename, image.getInputStream(), metadata);

        return true;
    }
}
