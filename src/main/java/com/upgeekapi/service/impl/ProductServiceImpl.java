package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.ProductRequestDTO;
import com.upgeekapi.entity.Product;
import com.upgeekapi.exception.custom.DataConflictException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.repository.ProductRepository;
import com.upgeekapi.repository.specification.ProductSpecification;
import com.upgeekapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementação da interface {@link ProductService}.
 * <p>
 * Esta classe orquestra a lógica de negócio para produtos, atuando como uma ponte
 * entre os controllers e a camada de persistência. Ela delega a construção de
 * entidades para a própria {@link Product} (Modelo de Domínio Rico) e a construção
 * de queries dinâmicas para o padrão {@link ProductSpecification}.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsOnSale() {
        return productRepository.findByOnSaleTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByTag(String tag) {
        return productRepository.findByTagsContaining(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        // Inicia com uma especificação que não filtra nada (WHERE 1=1).
        // A API foi simplificada, não sendo mais necessário usar o obsoleto Specification.where().
        Specification<Product> spec = ProductSpecification.conjunction();

        // Adiciona dinamicamente os filtros com base nos parâmetros fornecidos.
        if (name != null && !name.isBlank()) {
            spec = spec.and(ProductSpecification.nameLike(name));
        }
        if (minPrice != null) {
            spec = spec.and(ProductSpecification.priceGreaterThanOrEqual(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductSpecification.priceLessThanOrEqual(maxPrice));
        }

        return productRepository.findAll(spec);
    }

    @Override
    @Transactional
    public Product createProduct(ProductRequestDTO request) {
        // 1. Valida as regras de negócio antes de prosseguir.
        validateNameUniquenessOnCreate(request.name());

        // 2. Delega a criação da entidade para seu método de fábrica.
        Product newProduct = Product.from(request);

        // 3. Persiste a nova entidade.
        return productRepository.save(newProduct);
    }

    @Override
    @Transactional
    public Product updateProduct(Long productId, ProductRequestDTO request) {
        // 1. Busca a entidade a ser atualizada, garantindo que ela exista.
        Product productToUpdate = findProductOrThrow(productId);

        // 2. Valida as regras de negócio, como a unicidade do novo nome.
        validateNameUniquenessOnUpdate(request.name(), productId);

        // 3. Delega a lógica de atualização para a própria entidade.
        productToUpdate.updateFrom(request);

        // 4. Persiste as alterações.
        return productRepository.save(productToUpdate);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        // Garante que o produto exista antes de tentar deletar,
        // resultando em um erro 404 claro se o ID for inválido.
        Product productToDelete = findProductOrThrow(productId);
        productRepository.delete(productToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        return findProductOrThrow(productId);
    }

    /**
     * Valida se o nome de um novo produto já existe no banco de dados.
     *
     * @param name O nome a ser verificado.
     * @throws DataConflictException se o nome já estiver em uso.
     */
    private void validateNameUniquenessOnCreate(String name) {
        productRepository.findByName(name).ifPresent(p -> {
            throw new DataConflictException("Um produto com o nome '" + name + "' já existe.");
        });
    }

    /**
     * Valida se o nome de um produto sendo atualizado não entra em conflito com outro produto existente.
     *
     * @param name O novo nome a ser verificado.
     * @param currentProductId O ID do produto que está sendo atualizado, para excluí-lo da verificação.
     * @throws DataConflictException se o nome já estiver em uso por outro produto.
     */
    private void validateNameUniquenessOnUpdate(String name, Long currentProductId) {
        productRepository.findByName(name)
                // O filtro é crucial: só lança exceção se encontrar um produto com o mesmo nome E um ID diferente.
                .filter(foundProduct -> !foundProduct.getId().equals(currentProductId))
                .ifPresent(p -> {
                    throw new DataConflictException("O nome '" + name + "' já está em uso por outro produto.");
                });
    }

    /**
     * Método auxiliar privado para buscar um produto ou lançar uma exceção padronizada.
     * Centraliza a lógica de "encontrar ou falhar", mantendo o código dos métodos públicos mais limpo.
     *
     * @param productId O ID do produto a ser buscado.
     * @return A entidade {@link Product} encontrada.
     * @throws ResourceNotFoundException se o produto não for encontrado.
     */
    private Product findProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID '" + productId + "' não encontrado."));
    }
}