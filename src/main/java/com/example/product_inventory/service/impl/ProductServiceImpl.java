package com.example.product_inventory.service.impl;

import com.example.product_inventory.dto.CreateProductRequest;
import com.example.product_inventory.dto.LowStockProductDTO;
import com.example.product_inventory.dto.ProductDTO;
import com.example.product_inventory.dto.UpdateProductRequest;
import com.example.product_inventory.entity.Category;
import com.example.product_inventory.entity.Product;
import com.example.product_inventory.entity.Supplier;
import com.example.product_inventory.exception.ResourceNotFoundException;
import com.example.product_inventory.mapper.ProductMapper;
import com.example.product_inventory.repository.CategoryRepository;
import com.example.product_inventory.repository.ProductRepository;
import com.example.product_inventory.repository.SupplierRepository;
import com.example.product_inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDTO createProduct(CreateProductRequest request) {
        // Check if product with same name exists
        if (productRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Product with name '" + request.getName() + "' already exists");
        }

        Product product = productMapper.toEntity(request);
        product.setCategory(resolveCategory(request.getCategoryId()));
        product.setSuppliers(resolveSuppliers(request.getSupplierIds()));

        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> searchProducts(String query, Pageable pageable) {
        return productRepository.searchProducts(query, pageable)
                .map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LowStockProductDTO> getLowStockProducts(Integer threshold) {
        return productRepository.findLowStockWithCategoryAndSuppliers(threshold).stream()
                .map(productMapper::toLowStockDTO)
                .toList();
    }

    @Override
    public ProductDTO updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productMapper.updateEntity(product, request);

        if (request.getCategoryId() != null) {
            product.setCategory(resolveCategory(request.getCategoryId()));
        }
        if (request.getSupplierIds() != null) {
            product.setSuppliers(resolveSuppliers(request.getSupplierIds()));
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.toDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
    }

    private Category resolveCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
    }

    private Set<Supplier> resolveSuppliers(Set<Long> supplierIds) {
        if (supplierIds == null || supplierIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Supplier> suppliers = supplierRepository.findAllById(supplierIds);
        if (suppliers.size() != supplierIds.size()) {
            throw new ResourceNotFoundException("Supplier", "ids", supplierIds);
        }

        return new HashSet<>(suppliers);
    }
}