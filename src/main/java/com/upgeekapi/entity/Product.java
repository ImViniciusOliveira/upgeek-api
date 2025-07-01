package com.upgeekapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Representa um produto colecionável no e-commerce UpGeek.
 * Esta é uma entidade JPA, mapeada para a tabela "products" no banco de dados.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal originalPrice;

    @Column
    private BigDecimal discountPrice;

    @Column(nullable = false)
    private boolean onSale = false;

    @Column(nullable = false)
    private Long xp;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int stockQuantity;

    public Product(String name, String description, BigDecimal originalPrice, Long xp, String imageUrl, int stockQuantity) {
        this.name = name;
        this.description = description;
        this.originalPrice = originalPrice;
        this.xp = xp;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id != null && id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}