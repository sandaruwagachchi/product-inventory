package com.example.product_inventory.service.impl;

import com.example.product_inventory.dto.*;
import com.example.product_inventory.entity.Category;
import com.example.product_inventory.entity.Product;
import com.example.product_inventory.entity.Supplier;
import com.example.product_inventory.exception.ResourceNotFoundException;
import com.example.product_inventory.mapper.ProductMapper;
import com.example.product_inventory.repository.CategoryRepository;
import com.example.product_inventory.repository.ProductRepository;
import com.example.product_inventory.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDTO productDTO;
    private Category category;
    private Supplier supplier;
    private CreateProductRequest createRequest;
    private UpdateProductRequest updateRequest;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Tech Supplies Inc.");

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("High-performance laptop");
        product.setPrice(999.99);
        product.setStockQuantity(50);
        product.setCategory(category);
        product.setSuppliers(new HashSet<>(Set.of(supplier)));

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Laptop");
        productDTO.setDescription("High-performance laptop");
        productDTO.setPrice(999.99);
        productDTO.setStockQuantity(50);
        productDTO.setCategoryId(1L);
        productDTO.setCategoryName("Electronics");

        createRequest = new CreateProductRequest();
        createRequest.setName("Laptop");
        createRequest.setDescription("High-performance laptop");
        createRequest.setPrice(999.99);
        createRequest.setStockQuantity(50);
        createRequest.setCategoryId(1L);
        createRequest.setSupplierIds(new HashSet<>(Set.of(1L)));

        updateRequest = new UpdateProductRequest();
        updateRequest.setName("Gaming Laptop");
        updateRequest.setPrice(1299.99);
        updateRequest.setStockQuantity(30);
    }

    @Test
    void createProduct_Success() {
        when(productRepository.existsByName(createRequest.getName())).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(supplierRepository.findAllById(Set.of(1L))).thenReturn(List.of(supplier));
        when(productMapper.toEntity(any(CreateProductRequest.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDTO(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.createProduct(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Laptop");

        verify(productRepository).existsByName(createRequest.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_ThrowsException_WhenNameAlreadyExists() {
        when(productRepository.existsByName(createRequest.getName())).thenReturn(true);

        assertThatThrownBy(() -> productService.createProduct(createRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_ThrowsException_WhenCategoryNotFound() {
        when(productRepository.existsByName(createRequest.getName())).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.createProduct(createRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category");
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.getProductById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_ThrowsException_WhenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product");
    }

    @Test
    void getAllProducts_ReturnsPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toDTO(any(Product.class))).thenReturn(productDTO);

        Page<ProductDTO> result = productService.getAllProducts(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        verify(productRepository).findAll(pageable);
    }

    @Test
    void getProductsByCategory_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findByCategoryId(1L, pageable)).thenReturn(productPage);
        when(productMapper.toDTO(any(Product.class))).thenReturn(productDTO);

        Page<ProductDTO> result = productService.getProductsByCategory(1L, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        verify(productRepository).findByCategoryId(1L, pageable);
    }

    @Test
    void searchProducts_ReturnsMatchingProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        String query = "laptop";
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.searchProducts(query, pageable)).thenReturn(productPage);
        when(productMapper.toDTO(any(Product.class))).thenReturn(productDTO);

        Page<ProductDTO> result = productService.searchProducts(query, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        verify(productRepository).searchProducts(query, pageable);
    }

    @Test
    void updateProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDTO(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.updateProduct(1L, updateRequest);

        assertThat(result).isNotNull();
        verify(productMapper).updateEntity(product, updateRequest);
        verify(productRepository).save(product);
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }
}