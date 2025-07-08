package com.upgeekapi.service;

import com.upgeekapi.dto.request.ProductRequestDTO;
import com.upgeekapi.entity.Product; // Importamos a entidade

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface que define o contrato para os serviços relacionados a produtos.
 * <p>
 * Esta camada é responsável pela lógica de negócio e opera exclusivamente
 * com a entidade {@link Product}. A conversão para DTOs é delegada
 * para a camada de apresentação (Controller/Assembler).
 */
public interface ProductService {

    /**
     * Retorna uma lista de todas as entidades de produto.
     * @return Uma lista de {@link Product}.
     */
    List<Product> getAllProducts();

    /**
     * Retorna uma lista de todas as entidades de produto que estão em promoção.
     * @return Uma lista de {@link Product} em promoção.
     */
    List<Product> getProductsOnSale();

    /**
     * Busca e retorna uma lista de entidades de produto que contêm uma tag específica.
     * @param tag O nome da tag a ser buscada.
     * @return Uma lista de {@link Product} com a tag especificada.
     */
    List<Product> getProductsByTag(String tag);

    /**
     * Busca produtos com base em critérios de filtro dinâmicos.
     * @param name Critério de busca por nome (parcial).
     * @param minPrice Preço mínimo.
     * @param maxPrice Preço máximo.
     * @return Uma lista de {@link Product} que correspondem aos filtros.
     */
    List<Product> searchProducts(String name, BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Busca uma única entidade de produto pelo seu ID.
     * Essencial para endpoints de detalhe e para a construção de links HATEOAS.
     * @param productId O ID do produto a ser buscado.
     * @return A entidade {@link Product} encontrada.
     * @throws com.upgeekapi.exception.custom.ResourceNotFoundException se o produto não for encontrado.
     */
    Product getProductById(Long productId);

    /**
     * Cria uma nova entidade de produto com base nos dados de uma requisição.
     * @param request O DTO com os dados para a criação do produto.
     * @return A entidade {@link Product} recém-criada e persistida.
     */
    Product createProduct(ProductRequestDTO request);

    /**
     * Atualiza uma entidade de produto existente.
     * @param productId O ID do produto a ser atualizado.
     * @param request O DTO com os novos dados do produto.
     * @return A entidade {@link Product} atualizada.
     */
    Product updateProduct(Long productId, ProductRequestDTO request);

    /**
     * Deleta um produto pelo seu ID.
     * @param productId O ID do produto a ser deletado.
     */
    void deleteProduct(Long productId);
}