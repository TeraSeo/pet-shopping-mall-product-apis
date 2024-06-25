package com.shoppingmall.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<ProductDetail> productDetails;

    public void addProductDetail(ProductDetail productDetail) {
        productDetail.setProduct(this);
        getProductDetails().add(productDetail);
    }

    public void removeProductDetail(ProductDetail productDetail) {
        productDetail.setProduct(null);
        getProductDetails().remove(productDetail);
    }

}