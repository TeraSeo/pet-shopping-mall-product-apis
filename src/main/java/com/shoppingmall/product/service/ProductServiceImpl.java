package com.shoppingmall.product.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProductServiceImpl implements ProductService {

    @Value("${s3.bucket}")
    private String bucket;

    private final AmazonS3Client s3Client;

    @Autowired
    public ProductServiceImpl(AmazonS3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public Boolean addProduct() {
        return null;
    }

    @Override
    public String uploadProductImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        s3Client.putObject(bucket, originalFilename, file.getInputStream(), metadata);

        return s3Client.getUrl(bucket, originalFilename).toString();
    }
}
