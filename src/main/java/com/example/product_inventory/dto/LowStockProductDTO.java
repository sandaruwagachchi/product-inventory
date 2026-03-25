package com.example.product_inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LowStockProductDTO {
    private Long id;
    private String name;
    private Integer stockQuantity;
    private CategoryDTO category;
    private List<SupplierDTO> suppliers;
}

