package com.example.product_inventory.repository;

import com.example.product_inventory.entity.Category;
import com.example.product_inventory.entity.Product;
import com.example.product_inventory.entity.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
})
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private Category electronicsCategory;
    private Supplier supplier;
    private Product laptop;

    @BeforeEach
    void setUp() {
        electronicsCategory = new Category();
        electronicsCategory.setName("Electronics");
        entityManager.persist(electronicsCategory);

        supplier = new Supplier();
        supplier.setName("Tech Distributors Inc.");
        supplier.setContactEmail("contact@techdist.com");
        entityManager.persist(supplier);

        laptop = new Product();
        laptop.setName("Gaming Laptop");
        laptop.setDescription("High-performance gaming laptop");
        laptop.setPrice(1299.99);
        laptop.setStockQuantity(15);
        laptop.setCategory(electronicsCategory);
        laptop.setSuppliers(new HashSet<>(Set.of(supplier)));
        entityManager.persist(laptop);

        entityManager.flush();
    }

    @Test
    void searchProducts_ShouldFindProductsByNameIgnoreCase() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> result = productRepository.searchProducts("laptop", pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Gaming Laptop");
        assertThat(result.getContent().get(0).getCategory()).isNotNull();
        assertThat(result.getContent().get(0).getCategory().getName()).isEqualTo("Electronics");
        assertThat(result.getContent().get(0).getSuppliers()).hasSize(1);
    }
}