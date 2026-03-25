package com.example.product_inventory.repository;

import com.example.product_inventory.entity.Product;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManagerFactory;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void findLowStockWithCategoryAndSuppliers_fetchesAssociationsInSingleQuery() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics statistics = sessionFactory.getStatistics();
        statistics.clear();
        statistics.setStatisticsEnabled(true);

        List<Product> products = productRepository.findLowStockWithCategoryAndSuppliers(10);

        assertThat(products).isNotEmpty();
        products.forEach(product -> {
            assertThat(product.getCategory()).isNotNull();
            assertThat(product.getSuppliers()).isNotNull();
            product.getSuppliers().size();
            product.getCategory().getName();
        });

        long queryCount = statistics.getPrepareStatementCount();
        assertThat(queryCount).isEqualTo(1L);
    }
}

