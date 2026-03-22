package com.example.product_inventory.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @NotBlank(message = "Category is required")
    @Pattern(regexp = "^(ELECTRONICS|CLOTHING|BOOKS|FOOD|OTHER)$",
            message = "Category must be ELECTRONICS, CLOTHING, BOOKS, FOOD, or OTHER")
    private String category;
}