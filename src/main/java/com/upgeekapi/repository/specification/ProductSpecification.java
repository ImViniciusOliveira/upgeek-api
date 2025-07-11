package com.upgeekapi.repository.specification;

import com.upgeekapi.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;

/**
 * Fábrica de especificações ({@link Specification}) para a entidade {@link Product}.
 * <p>
 * Esta classe encapsula a lógica de construção de predicados de consulta complexos,
 * permitindo que a camada de serviço construa queries dinâmicas de forma declarativa
 * e segura, sem se acoplar aos detalhes da JPA Criteria API.
 */
public final class ProductSpecification {

    /**
     * Construtor privado para impedir a instanciação, reforçando o uso estático.
     */
    private ProductSpecification() {}

    /**
     * Retorna uma especificação neutra que é sempre verdadeira (semelhante a um `WHERE 1=1`).
     * <p>
     * É o ponto de partida ideal para construir queries dinâmicas, pois permite adicionar
     * outras especificações com `.and()` sem a necessidade de verificar se a especificação
     * inicial é nula.
     *
     * @return uma {@link Specification} que não aplica nenhum filtro.
     */
    public static Specification<Product> conjunction() {
        return (root, query, builder) -> builder.conjunction();
    }

    /**
     * Cria uma especificação que filtra produtos cujo nome contenha o texto fornecido,
     * ignorando maiúsculas e minúsculas (case-insensitive).
     *
     * @param name O texto a ser buscado no nome do produto.
     * @return A {@link Specification} correspondente.
     */
    public static Specification<Product> nameLike(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    /**
     * Cria uma especificação que filtra produtos cujo <b>preço ativo</b> (promocional ou original)
     * seja maior ou igual ao valor mínimo fornecido.
     *
     * @param minPrice O preço mínimo para a busca.
     * @return A {@link Specification} correspondente.
     */
    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(getActivePriceExpression(root, builder), minPrice);
    }

    /**
     * Cria uma especificação que filtra produtos cujo <b>preço ativo</b> (promocional ou original)
     * seja menor ou igual ao valor máximo fornecido.
     *
     * @param maxPrice O preço máximo para a busca.
     * @return A {@link Specification} correspondente.
     */
    public static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(getActivePriceExpression(root, builder), maxPrice);
    }

    /**
     * Constrói e retorna uma expressão da Criteria API que representa o preço ativo do produto.
     * <p>
     * Esta lógica é traduzida pelo JPA para uma cláusula SQL {@code CASE}, garantindo que a regra de negócio
     * (usar o preço com desconto se o produto estiver em promoção) seja executada de forma
     * eficiente diretamente no banco de dados.
     * <br>
     * SQL gerado: {@code CASE WHEN on_sale = true THEN discount_price ELSE original_price END}
     *
     * @param root O {@link Root} da query, de onde os campos da entidade são acessados.
     * @param builder O {@link CriteriaBuilder} usado para construir as expressões.
     * @return Uma {@link Expression} que representa o preço a ser usado nos filtros.
     */
    private static Expression<BigDecimal> getActivePriceExpression(Root<Product> root, CriteriaBuilder builder) {
        return builder.<BigDecimal>selectCase()
                .when(builder.isTrue(root.get("onSale")), root.get("discountPrice"))
                .otherwise(root.get("originalPrice"));
    }
}