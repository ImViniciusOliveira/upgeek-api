package com.upgeekapi.entity;

import com.upgeekapi.dto.request.ProductRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa um produto colecionável no e-commerce UpGeek.
 * <p>
 * Esta entidade segue um padrão mais imutável, onde as alterações de estado
 * são gerenciadas pelo serviço através do padrão Builder, garantindo um fluxo de
 * dados mais previsível.
 */
@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@ToString(exclude = "tags")
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
    private boolean onSale;

    @Column(nullable = false)
    private Long xp;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Integer stockQuantity;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    /**
     * Método de fábrica estático para criar uma nova instância de {@link Product} a partir de um DTO.
     * <p>
     * Encapsula a lógica de criação e garante que as regras de negócio (como a do preço com desconto)
     * sejam aplicadas desde o início.
     *
     * @param request O DTO com os dados para a criação.
     * @return Uma nova entidade {@link Product}, pronta para ser persistida.
     */
    public static Product from(ProductRequestDTO request) {
        boolean isOnSale = request.onSale();
        BigDecimal discountPrice = isOnSale ? request.discountPrice() : null;
        Set<String> tags = request.tags() != null ? new HashSet<>(request.tags()) : new HashSet<>();

        return Product.builder()
                .name(request.name())
                .description(request.description())
                .originalPrice(request.originalPrice())
                .discountPrice(discountPrice)
                .onSale(isOnSale)
                .xp(request.xp())
                .imageUrl(request.imageUrl())
                .stockQuantity(request.stockQuantity())
                .tags(tags)
                .build();
    }
}