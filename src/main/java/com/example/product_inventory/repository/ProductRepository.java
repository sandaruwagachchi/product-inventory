package com.example.product_inventory.repository;

import com.example.product_inventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Product> searchProducts(@Param("query") String query, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN FETCH p.category c " +
            "LEFT JOIN FETCH p.suppliers s " +
            "WHERE p.stockQuantity < :threshold " +
            "ORDER BY p.stockQuantity ASC")
    List<Product> findLowStockWithCategoryAndSuppliers(@Param("threshold") Integer threshold);

    boolean existsByName(String name);
}