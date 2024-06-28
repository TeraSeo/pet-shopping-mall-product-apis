package com.shoppingmall.product.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.shoppingmall.product.dto.ProductDto;
import com.shoppingmall.product.dto.ProductDtoWithoutImage;
import com.shoppingmall.product.entity.Product;
import com.shoppingmall.product.entity.ProductDetail;
import com.shoppingmall.product.entity.User;
import com.shoppingmall.product.repository.ProductDetailRepository;
import com.shoppingmall.product.repository.ProductRepository;
import com.shoppingmall.product.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ProductServiceImpl implements ProductService {

    @Value("${s3.bucket}")
    private String bucket;

    private final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final AmazonS3Client s3Client;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(AmazonS3Client s3Client, ProductRepository productRepository, ProductDetailRepository productDetailRepository, UserRepository userRepository) {
        this.s3Client = s3Client;
        this.productRepository = productRepository;
        this.productDetailRepository = productDetailRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Boolean addProduct(ProductDto productDto, String fileName) {
        Optional<Product> p = productRepository.findByCategory(productDto.getCategory());
        Product product;
        if (p.isPresent()) {
            product = p.get();
        }
        else {
            product = Product.builder()
                    .category(productDto.getCategory())
                    .productDetails(new HashSet<>())
                    .build();
            productRepository.save(product);
        }

        Optional<User> u = userRepository.findById(Long.valueOf(productDto.getUser_id()));
        User user = null;
        if (u.isPresent()) {
            user = u.get();
        }

        LOGGER.debug("file name: " + productDto.getImage().getOriginalFilename());

        ProductDetail productDetail = ProductDetail.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .deliveryFee(productDto.getDeliveryFee())
                .image(fileName)
                .subCategory(productDto.getSubCategory())
                .user(user)
                .build();

        product.addProductDetail(productDetail);
        user.addProductDetail(productDetail);

        productDetailRepository.save(productDetail);
        return true;
    }

    @Override
    public Boolean uploadProductImage(MultipartFile image, String fileName) throws IOException {

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        s3Client.putObject(bucket, fileName, image.getInputStream(), metadata);

        return true;
    }

    @Override
    public void deleteProductImage(String fileName) {
        s3Client.deleteObject(bucket, fileName);
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @Override
    public Boolean deleteProducts(List<String> productDetailIds, List<String> imagePaths) {
        int size = Math.min(productDetailIds.size(), imagePaths.size());
        for (int i = 0; i < size; i++) {
            String id = productDetailIds.get(i);
            String path = imagePaths.get(i);

            Optional<ProductDetail> p = productDetailRepository.findById(Long.valueOf(id));
            if (p.isPresent()) {
                ProductDetail productDetail = p.get();
                Product product = productDetail.getProduct();
                User user = productDetail.getUser();

                product.removeProductDetail(productDetail);
                user.removeProductDetail(productDetail);

                productRepository.save(product);
            }

            deleteProductImage(path);
        }
        return true;
    }

    @Override
    public Boolean editProduct(ProductDto productDto) throws IOException {
        Long id = productDto.getId();
        Optional<ProductDetail> p = productDetailRepository.findById(id);
        if (p.isPresent()) {
            Random random = new Random();
            String fileName = productDto.getImage().getOriginalFilename() + random.nextInt(100000);
            ProductDetail productDetail = p.get();
            productDetail.setName(productDto.getName());
            productDetail.setQuantity(productDto.getQuantity());
            productDetail.setPrice(productDto.getPrice());
            productDetail.setSubCategory(productDto.getSubCategory());
            productDetail.setDeliveryFee(productDto.getDeliveryFee());
            productDetail.setImage(fileName);

            Product product;
            Optional<Product> o = productRepository.findByCategory(productDto.getCategory());
            if (o.isEmpty()) {
                product = Product.builder()
                        .category(productDto.getCategory())
                        .productDetails(new HashSet<>())
                        .build();
                productRepository.save(product);
            }
            else {
                product = o.get();
            }

            product.addProductDetail(productDetail);
            productDetailRepository.save(productDetail);

            deleteProductImage(productDto.getImagePath());
            uploadProductImage(productDto.getImage(), fileName);

            return true;
        }
        return false;
    }

    @Override
    public Boolean editProductWithoutImage(ProductDtoWithoutImage productDto) {
        Long id = productDto.getId();
        Optional<ProductDetail> p = productDetailRepository.findById(id);
        if (p.isPresent()) {
            ProductDetail productDetail = p.get();
            productDetail.setName(productDto.getName());
            productDetail.setQuantity(productDto.getQuantity());
            productDetail.setPrice(productDto.getPrice());
            productDetail.setSubCategory(productDto.getSubCategory());
            productDetail.setDeliveryFee(productDto.getDeliveryFee());

            Product product;
            Optional<Product> o = productRepository.findByCategory(productDto.getCategory());
            if (o.isEmpty()) {
                product = Product.builder()
                        .category(productDto.getCategory())
                        .productDetails(new HashSet<>())
                        .build();
                productRepository.save(product);
            }
            else {
                product = o.get();
            }

            product.addProductDetail(productDetail);
            productDetailRepository.save(productDetail);
            return true;
        }
        return false;
    }

    @Override
    public List<ProductDetail> findProductsBySubCategory(String subCategory) {
        List<ProductDetail> productDetails = productDetailRepository.findAllBySubCategory(subCategory);
        LOGGER.debug("size: " + productDetails.size());
        return productDetails;
    }
}