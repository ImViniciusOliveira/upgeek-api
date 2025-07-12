package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.ProductSearchRequestDto;
import com.upgeekapi.entity.Product;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.repository.ProductRepository;
import com.upgeekapi.repository.specification.ProductSpecification;
import com.upgeekapi.service.ProductSearcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementação do serviço de busca de produtos.
 * <p>
 * Esta classe é responsável por traduzir os critérios de um {@link ProductSearchRequestDto}
 * em uma {@link Specification} do Spring Data JPA, permitindo a construção
 * de queries dinâmicas e seguras para a busca no banco de dados.
 */
@Component
@RequiredArgsConstructor
public class ProductSearcherImpl implements ProductSearcherService {

    private final ProductRepository productRepository;

    /**
     * {@inheritDoc}
     * <p>
     * Constrói dinamicamente uma {@link Specification} adicionando cláusulas 'AND'
     * para cada critério de busca não nulo fornecido no DTO.
     */
    @Override
    public List<Product> searchOrFail(ProductSearchRequestDto criteria) {
        // Inicia com uma especificação "conjunction", que é uma base neutra (semelhante a "WHERE 1=1")
        // para adicionar cláusulas 'AND' de forma segura.
        Specification<Product> spec = ProductSpecification.conjunction();

        // Adiciona o filtro de nome se ele for fornecido e não estiver em branco.
        if (criteria.name() != null && !criteria.name().isBlank()) {
            spec = spec.and(ProductSpecification.nameLike(criteria.name()));
        }
        // Adiciona o filtro de preço mínimo se fornecido.
        if (criteria.minPrice() != null) {
            spec = spec.and(ProductSpecification.priceGreaterThanOrEqual(criteria.minPrice()));
        }
        // Adiciona o filtro de preço máximo se fornecido.
        if (criteria.maxPrice() != null) {
            spec = spec.and(ProductSpecification.priceLessThanOrEqual(criteria.maxPrice()));
        }

        List<Product> results = productRepository.findAll(spec);

        // Lança uma exceção se a busca não retornar resultados, garantindo que o cliente
        // receba um feedback claro (404 Not Found).
        if (results.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum produto encontrado com os critérios informados.");
        }
        return results;
    }
}