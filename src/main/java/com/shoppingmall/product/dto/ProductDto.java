package com.shoppingmall.product.dto;

import com.shoppingmall.product.entity.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String summary;
    private int quantity;
    private int price;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String subCategory;
    private int deliveryFee;
    private MultipartFile image;
    private String user_id;
    private String imagePath;
}
