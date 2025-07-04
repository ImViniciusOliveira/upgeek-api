package com.upgeekapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa um produto colecionável no e-commerce UpGeek.
 * Esta é uma entidade JPA, mapeada para a tabela "products" no banco de dados.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
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
    @Builder.Default
    private boolean onSale = false;

    @Column(nullable = false)
    private Long xp;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int stockQuantity;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    @Builder.Default
    private Set<String> tags = new HashSet<>();
}