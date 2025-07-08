package com.upgeekapi.repository.specification;

import com.upgeekapi.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;

/**
 * Classe utilitária para criar instâncias de {@link Specification} para a entidade {@link Product}.
 * <p>
 * Centraliza toda a lógica de construção de queries dinâmicas, tornando o código
 * do serviço mais limpo e declarativo.
 */
public final class ProductSpecification {

    /**
     * Construtor privado para impedir a instanciação desta classe utilitária.
     */
    private ProductSpecification() {}

    /**
     * Retorna uma especificação base que não aplica nenhum filtro.
     * É o ponto de partida ideal para construir queries dinâmicas de forma segura.
     *
     * @return uma {@link Specification} que é sempre verdadeira.
     */
    public static Specification<Product> conjunction() {
        return (root, query, builder) -> builder.conjunction();
    }

    /**
     * Cria uma especificação para buscar produtos cujo nome contenha o texto fornecido (case-insensitive).
     *
     * @param name O texto a ser buscado no nome do produto.
     * @return A {@link Specification} correspondente.
     */
    public static Specification<Product> nameLike(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    /**
     * Cria uma especificação para buscar produtos cujo preço ativo seja maior ou igual ao valor fornecido.
     *
     * @param minPrice O preço mínimo para a busca.
     * @return A {@link Specification} correspondente.
     */
    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(getActivePriceExpression(root, builder), minPrice);
    }

    /**
     * Cria uma especificação para buscar produtos cujo preço ativo seja menor ou igual ao valor fornecido.
     *
     * @param maxPrice O preço máximo para a busca.
     * @return A {@link Specification} correspondente.
     */
    public static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(getActivePriceExpression(root, builder), maxPrice);
    }

    /**
     * Método auxiliar privado que encapsula a lógica para determinar o "preço ativo" de um produto.
     * <p>
     * Isso gera uma expressão SQL "CASE WHEN on_sale = true THEN discount_price ELSE original_price END",
     * que é a forma correta e performática de implementar essa regra de negócio no banco de dados.
     *
     * @param root O Root da query.
     * @param builder O CriteriaBuilder da query.
     * @return Uma {@link Expression} que representa o preço a ser usado nos filtros.
     */
    private static Expression<BigDecimal> getActivePriceExpression(Root<Product> root, CriteriaBuilder builder) {
        return builder.<BigDecimal>selectCase()
                .when(builder.isTrue(root.get("onSale")), root.get("discountPrice"))
                .otherwise(root.get("originalPrice"));
    }
}