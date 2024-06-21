package com.shoppingmall.product.repository;

import com.shoppingmall.product.entity.Category;
import com.shoppingmall.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCategory(Category category);

}
