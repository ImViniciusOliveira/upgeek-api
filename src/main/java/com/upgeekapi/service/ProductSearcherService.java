package com.upgeekapi.service;

import com.upgeekapi.dto.request.ProductSearchRequestDto;
import com.upgeekapi.entity.Product;
import java.util.List;

/**
 * Interface que define o contrato para um serviço especializado em busca de produtos.
 * <p>
 * Isola a complexidade da construção de queries dinâmicas (Specifications)
 * da lógica de negócio principal do {@link ProductService}, seguindo o
 * Princípio da Responsabilidade Única (SRP).
 */
public interface ProductSearcherService {

    /**
     * Executa uma busca dinâmica por produtos com base nos critérios fornecidos e falha se nenhum resultado for encontrado.
     *
     * @param criteria O DTO contendo todos os critérios de busca (nome, preço, etc.).
     * @return Uma lista de entidades {@link Product} que correspondem aos critérios.
     * @throws com.upgeekapi.exception.custom.ResourceNotFoundException se a busca não retornar nenhum produto.
     */
    List<Product> searchOrFail(ProductSearchRequestDto criteria);
}