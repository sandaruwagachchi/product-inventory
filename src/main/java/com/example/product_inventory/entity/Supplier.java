package com.example.product_inventory.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "contact_email", unique = true, length = 150)
    private String contactEmail;

    @ManyToMany(mappedBy = "suppliers", fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();
}

