package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.ProductRequestDTO;
import com.upgeekapi.dto.request.ProductSearchRequestDto;
import com.upgeekapi.entity.Product;
import com.upgeekapi.exception.custom.DataConflictException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.repository.ProductRepository;
import com.upgeekapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

/**
 * Implementação da interface {@link ProductService}.
 * <p>
 * Esta classe orquestra a lógica de negócio para produtos, atuando como uma ponte
 * entre os controllers e a camada de persistência. Ela delega a construção de
 * entidades para a própria {@link Product} e gerencia as atualizações através do
 * padrão Builder para promover um fluxo de dados mais imutável.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductSearcherImpl productSearcher;

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
        List<Product> products = productRepository.findByTagsContaining(tag);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum produto encontrado com a tag '" + tag + "'.");
        }
        return products;
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

    /**
     * Atualiza uma entidade de produto existente usando o padrão Builder.
     * <p>
     * Este método cria uma nova instância da entidade com os dados atualizados,
     * promovendo a imutabilidade e tornando o fluxo de dados mais previsível.
     *
     * @param productId O ID do produto a ser atualizado.
     * @param request O DTO com os novos dados do produto.
     * @return A entidade {@link Product} atualizada.
     */
    @Override
    @Transactional
    public Product updateProduct(Long productId, ProductRequestDTO request) {
        // 1. Busca a entidade atual, garantindo que ela exista.
        Product currentProduct = findProductOrThrow(productId);

        // 2. Valida as regras de negócio, como a unicidade do novo nome.
        validateNameUniquenessOnUpdate(request.name(), productId);

        // 3. Usa o padrão toBuilder() para criar uma cópia atualizada da entidade.
        // Este método cria um novo builder pré-populado com os dados de 'currentProduct'.
        Product updatedProduct = currentProduct.toBuilder()
                .name(request.name())
                .description(request.description())
                .originalPrice(request.originalPrice())
                .onSale(request.onSale())
                // Aplica a regra de negócio do preço com desconto.
                .discountPrice(request.onSale() ? request.discountPrice() : null)
                .xp(request.xp())
                .imageUrl(request.imageUrl())
                .stockQuantity(request.stockQuantity())
                .tags(request.tags() != null ? new HashSet<>(request.tags()) : new HashSet<>())
                .build(); // .build() cria a nova instância da entidade com os dados combinados.

        // 4. Persiste a entidade atualizada. O JPA é inteligente e fará um UPDATE no banco.
        return productRepository.save(updatedProduct);
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
        productRepository.findByName(name)
                .ifPresent(p -> {
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
                .orElseThrow(() ->
                        new ResourceNotFoundException("Produto com ID '" + productId + "' não encontrado."));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Delega a lógica de busca complexa para o ProductSearcherService,
     * mantendo o {@link ProductServiceImpl} focado em orquestrar as operações
     * de negócio principais (CRUD).
     */
    @Override
    public List<Product> searchProducts(ProductSearchRequestDto searchDto) {
        // Delega a execução da busca para o serviço especializado, mantendo este serviço coeso.
        return productSearcher.searchOrFail(searchDto);
    }
}