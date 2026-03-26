package com.example.product_inventory.mapper;

import com.example.product_inventory.dto.*;
import com.example.product_inventory.entity.Product;
import com.example.product_inventory.entity.Supplier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
        return dto;
    }

    public LowStockProductDTO toLowStockDTO(Product product) {
        if (product == null) return null;

        CategoryDTO categoryDTO = new CategoryDTO(
                product.getCategory().getId(),
                product.getCategory().getName()
        );

        List<SupplierDTO> suppliers = product.getSuppliers() == null
                ? Collections.emptyList()
                : product.getSuppliers().stream()
                .map(this::toSupplierDTO)
                .toList();

        return new LowStockProductDTO(
                product.getId(),
                product.getName(),
                product.getStockQuantity(),
                categoryDTO,
                suppliers
        );
    }

    public Product toEntity(CreateProductRequest request) {
        if (request == null) return null;

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        return product;
    }

    public void updateEntity(Product product, UpdateProductRequest request) {
        if (request == null) return;

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }
    }

    private SupplierDTO toSupplierDTO(Supplier supplier) {
        return new SupplierDTO(supplier.getId(), supplier.getName(), supplier.getContactEmail());
    }
}