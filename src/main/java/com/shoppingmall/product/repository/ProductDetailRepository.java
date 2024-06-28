package com.shoppingmall.product.repository;

import com.shoppingmall.product.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

    List<ProductDetail> findAllBySubCategory(String subCategory);

}
