package com.shoppingmall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShoppingMallProductApIsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingMallProductApIsApplication.class, args);
	}

}