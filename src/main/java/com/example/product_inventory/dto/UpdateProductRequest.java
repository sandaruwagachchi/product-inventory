package com.example.product_inventory.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateProductRequest {

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Positive(message = "Price must be positive")
    private Double price;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @Pattern(regexp = "^(ELECTRONICS|CLOTHING|BOOKS|FOOD|OTHER)$",
            message = "Category must be ELECTRONICS, CLOTHING, BOOKS, FOOD, or OTHER")
    private String category;
}