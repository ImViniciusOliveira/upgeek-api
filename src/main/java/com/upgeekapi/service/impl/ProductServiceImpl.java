package com.upgeekapi.service.impl;

import com.upgeekapi.dto.request.ProductRequestDTO;
import com.upgeekapi.entity.Product;
import com.upgeekapi.exception.custom.DataConflictException;
import com.upgeekapi.exception.custom.ResourceNotFoundException;
import com.upgeekapi.mapper.ProductMapper;
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
 * A responsabilidade desta classe é aplicar a lógica de negócio e orquestrar
 * a persistência de dados, trabalhando exclusivamente com a entidade {@link Product}.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

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
        Specification<Product> spec = ProductSpecification.conjunction();

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
        productRepository.findByName(request.name()).ifPresent(p -> {
            throw new DataConflictException("Um produto com o nome '" + request.name() + "' já existe.");
        });

        Product newProduct = productMapper.toEntity(request);
        return productRepository.save(newProduct);
    }

    @Override
    @Transactional
    public Product updateProduct(Long productId, ProductRequestDTO request) {
        Product productToUpdate = getProductById(productId);

        productRepository.findByName(request.name())
                .filter(foundProduct -> !foundProduct.getId().equals(productId))
                .ifPresent(p -> {
                    throw new DataConflictException("O nome '" + request.name() + "' já está em uso por outro produto.");
                });

        productMapper.updateProductFromDto(request, productToUpdate);
        return productRepository.save(productToUpdate);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product productToDelete = getProductById(productId);
        productRepository.delete(productToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID '" + productId + "' não encontrado."));
    }
}