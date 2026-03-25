package com.example.product_inventory.service;

import com.example.product_inventory.dto.CreateProductRequest;
import com.example.product_inventory.dto.LowStockProductDTO;
import com.example.product_inventory.dto.ProductDTO;
import com.example.product_inventory.dto.UpdateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(CreateProductRequest request);
    ProductDTO getProductById(Long id);
    Page<ProductDTO> getAllProducts(Pageable pageable);
    Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable);
    Page<ProductDTO> searchProducts(String query, Pageable pageable);
    List<LowStockProductDTO> getLowStockProducts(Integer threshold);
    ProductDTO updateProduct(Long id, UpdateProductRequest request);
    void deleteProduct(Long id);
}