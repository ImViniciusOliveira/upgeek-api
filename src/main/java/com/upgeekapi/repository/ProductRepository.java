package com.upgeekapi.repository;

import com.upgeekapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de acesso a dados da entidade {@link Product}.
 * <p>
 * Estende {@link JpaRepository} para funcionalidades CRUD padrão e
 * {@link JpaSpecificationExecutor} para permitir a construção de queries dinâmicas
 * e complexas através do padrão Specification (ver {@link com.upgeekapi.repository.specification.ProductSpecification}).
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * Busca um produto pelo seu nome único.
     * <p>
     * Essencial para validar a unicidade do nome de um produto antes de criá-lo ou atualizá-lo,
     * prevenindo a duplicidade de dados. A busca é sensível a maiúsculas e minúsculas.
     *
     * @param name O nome exato do produto a ser buscado.
     * @return um {@link Optional} contendo o produto se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<Product> findByName(String name);

    /**
     * Retorna uma lista de todos os produtos que estão atualmente marcados como em promoção.
     * <p>
     * Útil para popular seções de destaque, páginas de ofertas ou aplicar lógicas de negócio
     * específicas para itens em promoção.
     *
     * @return uma {@link List} de produtos em promoção.
     */
    List<Product> findByOnSaleTrue();

    /**
     * Retorna uma lista de produtos que contêm uma tag específica em sua coleção de tags.
     * <p>
     * Permite a filtragem de produtos por categoria ou característica, sendo fundamental
     * para a navegação e descoberta de itens na plataforma.
     *
     * @param tag A tag a ser buscada na coleção de tags dos produtos.
     * @return uma {@link List} de produtos que contêm a tag especificada.
     */
    List<Product> findByTagsContaining(String tag);
}