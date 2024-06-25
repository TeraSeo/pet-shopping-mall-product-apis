package com.shoppingmall.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean isVerified;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.PERSIST)
    private Set<ProductDetail> productDetails = new HashSet<>();

    public void addProductDetail(ProductDetail productDetail) {
        productDetail.setUser(this);
        getProductDetails().add(productDetail);
        updateModifiedDate();
    }

    public void removeProductDetail(ProductDetail productDetail) {
        productDetail.setUser(null);
        getProductDetails().remove(productDetail);
        updateModifiedDate();
    }

    public User updateModifiedDate() {
        this.onPreUpdate();
        return this;
    }

}
