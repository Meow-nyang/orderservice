package com.playdata.orderservice.product.repository;

import com.playdata.orderservice.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(false)

class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void bulkInsert() {
        for (int i = 1; i <= 50 ; i++) {
            Product p = Product.builder()
                    .name("상품" + i)
                    .category("카테고리" + i)
                    .price(3000)
                    .stockQuantity(100)
                    .build();
            productRepository.save(p);
        }
    }
}