package com.shoppingmall.product.dto;

import com.shoppingmall.product.entity.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class ProductDto {

    private String name;
    private String summary;
    private int quantity;
    private int price;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String subCategory;
    private int deliveryFee;
    private MultipartFile image;
}
