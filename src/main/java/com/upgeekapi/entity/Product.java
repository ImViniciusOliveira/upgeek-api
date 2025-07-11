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
 * Esta é uma entidade JPA, mapeada para a tabela "products". A ausência de setters
 * públicos é uma decisão de design deliberada para promover a imutabilidade e garantir
 * que as alterações de estado sejam controladas por métodos de negócio explícitos,
 * como o método de fábrica {@link #from(ProductRequestDTO)} e o de atualização {@link #updateFrom(ProductRequestDTO)}.
 */
@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Construtor sem argumentos protegido para uso exclusivo do JPA/Hibernate.
@AllArgsConstructor(access = AccessLevel.PRIVATE)  // Construtor completo privado para ser usado apenas pelo padrão Builder.
@Builder(toBuilder = true)
@ToString(exclude = "tags") // Exclui a coleção do toString() para evitar LazyInitializationException ao logar a entidade fora de uma transação.
@EqualsAndHashCode(of = "id") // Define a igualdade baseada apenas no ID, que é a prática recomendada para entidades JPA.
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

    // FetchType.LAZY é a melhor prática para coleções para evitar problemas de performance (N+1).
    // A coleção será carregada do banco de dados apenas quando for explicitamente acessada pelo código.
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    /**
     * Atualiza os campos da entidade a partir de um DTO de requisição.
     * <p>
     * Este método centraliza a lógica de atualização, garantindo que a entidade
     * permaneça em um estado consistente. A lógica de negócio para o preço de
     * desconto é aplicada aqui.
     *
     * @param request O DTO contendo os novos dados do produto.
     */
    public void updateFrom(ProductRequestDTO request) {
        this.name = request.name();
        this.description = request.description();
        this.originalPrice = request.originalPrice();
        this.onSale = request.onSale();
        // Regra de negócio: o preço com desconto só é definido se o produto estiver em promoção.
        this.discountPrice = this.onSale ? request.discountPrice() : null;
        this.xp = request.xp();
        this.imageUrl = request.imageUrl();
        this.stockQuantity = request.stockQuantity();

        // Garante que a coleção de tags seja sincronizada com o DTO.
        this.tags.clear();
        if (request.tags() != null) {
            this.tags.addAll(request.tags());
        }
    }

    /**
     * Método de fábrica estático para criar uma nova instância de {@link Product} a partir de um DTO.
     * <p>
     * Encapsula a lógica de criação e garante que as regras de negócio (como a do preço com desconto)
     * sejam aplicadas desde o início. Este método é o ponto de entrada oficial para criar novas entidades.
     *
     * @param request O DTO com os dados para a criação.
     * @return Uma nova entidade {@link Product}, pronta para ser persistida.
     */
    public static Product from(ProductRequestDTO request) {
        boolean isOnSale = request.onSale();
        // Aplica regra de negócio do preço com desconto durante a criação.
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